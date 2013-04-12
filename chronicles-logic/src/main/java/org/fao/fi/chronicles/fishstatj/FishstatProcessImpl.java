package org.fao.fi.chronicles.fishstatj;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.chronicles.container.ChroniclesException;
import org.fao.fi.fishstat.data.common.api.IdentifierFactory;
import org.fao.fi.fishstat.data.reference.api.Concept;
import org.fao.fi.fishstat.data.reference.api.ReferenceObject;
import org.fao.fi.fishstat.data.reference.api.ReferenceServiceFactory;
import org.fao.fi.fishstat.data.reference.api.Relationship;
import org.fao.fi.fishstat.data.timeseries.api.Dataset;
import org.fao.fi.fishstat.data.timeseries.api.Measure;
import org.fao.fi.fishstat.data.timeseries.api.ObservationPeriod;
import org.fao.fi.fishstat.data.timeseries.api.ObservationSeries;
import org.fao.fi.fishstat.data.timeseries.api.Selection;
import org.fao.fi.fishstat.data.timeseries.api.TimeResolution;
import org.fao.fi.fishstat.data.timeseries.api.Timeseries;
import org.fao.fi.fishstat.data.timeseries.api.TimeseriesServiceFactory;
import org.fao.fi.list2tabular.ListTabular;
import org.fao.fi.tabulardata.model.TabularSeries;
import org.fao.fi.tabularseries.metamodel.Attribute;
import org.fao.fi.tabularseries.metamodel.ColumnDimension;
import org.fao.fi.tabularseries.metamodel.Dimension;
import org.fao.fi.tabularseries.metamodel.SeriesMetadata;
import org.fao.fi.tabularseries.metamodel.Value;
import org.springframework.stereotype.Component;

import com.googlecode.ehcache.annotations.Cacheable;

/**
 * Reflects all the logic done in FishstatJ, for the chronicles project.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
@Component
public class FishstatProcessImpl implements FishstatProcess {

    /**
     * 
     * See for the un codes as well the http://www.fao.org/countryprofiles/geoinfo/ws/countryCodes/ITA
     * 
     * @param startYear
     * @param endYear
     * @param unCodes
     * @return
     */
    @Cacheable(cacheName = "ChroniclesCache")
    public FishstatProcessResult run(int startYear, int endYear, String[] unCodes) {
        Timeseries timeseries = retrieveTimeseries();
        filterOnMarineOnly(timeseries);
        filterOverCountries(timeseries, unCodes);
        aggregateOverCountries(timeseries);
        FishstatProcessResult result = timeseries2FishstatProcessResult(timeseries, startYear, endYear);
        timeseries.reset();
        return result;
    }

    private void filterOverCountries(Timeseries timeseries, String[] unCodeCountries) {
        if (unCodeCountries != null && !(unCodeCountries.length == 1 && unCodeCountries[0] == null)) {
            int originalSize = timeseries.getObservations().size();

            // add Country aggregation
            Concept country = ReferenceServiceFactory.getService().getConcept("COUNTRY");

            // build up the aggregation
            Selection selection = Selection.instance();
            for (String unCodeCounty : unCodeCountries) {
                // this one gives still an error.
                selection.add(country, country.getObject("ALPHA_3_UN_CODE", unCodeCounty));
                // String unCountryCode = new Long(unCodeCounty).longValue();
                // selection.add(country, country.getObject("UN_CODE", unCodeCounty));
            }
            // perform the selection
            timeseries.filter(selection);

            validateSize(originalSize, timeseries.getObservations().size());
        }
    }

    private Timeseries retrieveTimeseries() {
        Dataset dataset = TimeseriesServiceFactory.getService().getDatasetByAcronym("CAPTURE");
        Timeseries timeseries = dataset.getTimeseries("QUANTITY");
        if (timeseries.getObservations().size() == 0) {
            throw new ChroniclesException("Nr of observations is null");
        }
        return timeseries;
    }

    /**
     * 10 is
     * 
     * 
     * @param timeseries
     */
    private void filterOnMarineOnly(Timeseries timeseries) {
        int originalSize = timeseries.getObservations().size();
        // INLAND_MARINE refers to the group of marine and inland waters
        Concept inlandMarine = ReferenceServiceFactory.getService().getConcept("INLAND_MARINE");
        Concept area = ReferenceServiceFactory.getService().getConcept("AREA");
        // INLAND_MARINE_AREA has the relationship between area-inland and area-marine
        Relationship relationship = ReferenceServiceFactory.getService().getRelationship("INLAND_MARINE_AREA");
        ReferenceObject marineOnly = inlandMarine.getObject("CODE", new Long(10));
        Selection selection = Selection.instance();
        for (ReferenceObject roArea : relationship.getChildren(marineOnly)) {
            selection.add(area, roArea);
        }
        timeseries.filter(selection);
        // String where_clause = "bigint(area) > ? ";
        // Object[] where_params = { new Integer(9) };
        // timeseries.select(where_clause, where_params);
        validateSize(originalSize, timeseries.getObservations().size());
    }

    private void aggregateOverCountries(Timeseries timeseries) {
        int originalSize = timeseries.getObservations().size();
        Concept countryConcept = ReferenceServiceFactory.getService().getConcept("COUNTRY");
        Selection aggregation = Selection.instance();
        aggregation.add(countryConcept, ReferenceObject.ALL);
        timeseries.aggregate(aggregation);
        validateSize(originalSize, timeseries.getObservations().size());

    }

    private FishstatProcessResult timeseries2FishstatProcessResult(Timeseries timeseries, int startYear, int endYear) {
        Concept speciesConcept = ReferenceServiceFactory.getService().getConcept("SPECIES");
        Concept areaConcept = ReferenceServiceFactory.getService().getConcept(FishstatJConcepts.AREA);
        Concept measureConcept = ReferenceServiceFactory.getService().getConcept(FishstatJConcepts.MEASURE);
        FishstatProcessResult result = new FishstatProcessResult();
        List<ObservationSeries> observations = timeseries.getObservations();
        String range = "Y" + startYear + "..Y" + endYear;
        List<ObservationPeriod> observationPeriods = ObservationPeriod.parseRange(range, TimeResolution.YEAR);

        List<List<String>> array = new ArrayList<List<String>>();

        for (ObservationPeriod observationPeriod : observationPeriods) {
            for (ObservationSeries observationSeries : observations) {
                List<String> row = new ArrayList<String>();
                Measure measure = observationSeries.getMeasure(observationPeriod);
                if (measure != null) {
                    // species
                    ReferenceObject speciesRo = observationSeries.getKey(speciesConcept);
                    ReferenceObject measureRo = observationSeries.getKey(measureConcept);
                    String unitOfMeasure = measureRo.getAttribute("UNIT").toString();
                    // unit of meausere taken into account is t (tonnes)
                    if (higherThanGenus(speciesRo) && unitOfMeasure.equals("t")) {
                        String value = "";

                        // species alpha 3 code
                        if (speciesRo != null && speciesRo.getAttribute(FishstatJConcepts.ALPHA_3_CODE) != null) {
                            value = speciesRo.getAttribute(FishstatJConcepts.ALPHA_3_CODE).toString();
                        }
                        row.add(value);

                        // scientificName
                        if (speciesRo != null && speciesRo.getAttribute("SCIENTIFIC_NAME") != null) {
                            value = speciesRo.getAttribute(FishstatJConcepts.SCIENTIFIC_NAME).toString();
                        }
                        row.add(value);

                        // area
                        ReferenceObject areaRo = observationSeries.getKey(areaConcept);
                        if (areaRo != null && areaRo.getAttribute("CODE") != null) {
                            value = areaRo.getAttribute("CODE").toString();
                        }
                        row.add(value);

                        // capture
                        row.add(new Double(measure.getValue()).toString());

                        // time
                        row.add(observationPeriod.getYear());

                        array.add(row);
                    }
                }
            }
        }
        TabularSeries tabularSeries = new TabularSeries();
        tabularSeries.setTable(ListTabular.ListList2TabularSeries(array, defineSeriesMetadata()));
        result.setTabularSeries(tabularSeries);
        result.setSeriesMetadata(defineSeriesMetadata());
        return result;
    }

    private boolean higherThanGenus(ReferenceObject speciesRo) {
        boolean validForAnalysis = false;
        if (speciesRo != null && speciesRo.getAttribute(FishstatJConcepts.TAX_CODE) != null) {
            String value = speciesRo.getAttribute(FishstatJConcepts.TAX_CODE).toString();
            if (!value.contains("XXX")) {
                validForAnalysis = true;
            }
        }
        // id check the id. Delete all species which do indicate a level higher than the genus. These levels
        // are indicated with a number of X characters more than 2 in a row.
        // boolean idGenusCorrect = true;
        // if (speciesRo != null && speciesRo.getAttribute("ID") != null) {
        // String id = speciesRo.getAttribute("ID").toString();
        // idGenusCorrect = !id.contains("XXX");
        // }
        return validForAnalysis;
    }

    private SeriesMetadata defineSeriesMetadata() {
        List<org.fao.fi.tabularseries.metamodel.Concept> headerList = new ArrayList<org.fao.fi.tabularseries.metamodel.Concept>();
        Dimension d1 = new Dimension(FishstatJConcepts.ALPHA_3_CODE);
        Attribute a1 = new Attribute(FishstatJConcepts.SCIENTIFIC_NAME);
        Dimension d2 = new Dimension(FishstatJConcepts.AREA);
        Value v1 = new Value(FishstatJConcepts.CATCH);
        ColumnDimension d3 = new ColumnDimension(FishstatJConcepts.YEAR);
        headerList.add(d1);
        headerList.add(a1);
        headerList.add(d2);
        headerList.add(v1);
        headerList.add(d3);
        SeriesMetadata smd = new SeriesMetadata(headerList);
        return smd;
    }

    private void validateSize(int previousSize, int newSize) {
        if (previousSize < newSize) {
            throw new ChroniclesException("Operation enlarged the collection, bug!");
        }
    }

}
