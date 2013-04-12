package org.fao.fi.chronicles.calcuation;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.chronicles.fishstatj.FishstatProcess;
import org.fao.fi.chronicles.fishstatj.FishstatProcessResult;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.tabular2pivot.Csv2Tabular;
import org.fao.fi.tabular2pivot.TabularSeries2Pivot;
import org.fao.fi.tabulardata.model.TabularSeries;
import org.fao.fi.tabularseries.metamodel.Attribute;
import org.fao.fi.tabularseries.metamodel.ColumnDimension;
import org.fao.fi.tabularseries.metamodel.Dimension;
import org.fao.fi.tabularseries.metamodel.SeriesMetadata;
import org.fao.fi.tabularseries.metamodel.Value;

public class FishstatProcessTestHelper implements FishstatProcess {

    /**
     * 
     * ALPHA_3_CODE,SCIENTIFIC_NAME, AREA, CATCH, YEAR MIW, Balaenoptera acutorostrata,98, 0.0, 1950
     * 
     */

    @Override
    public FishstatProcessResult run(int startYear, int endYear, String[] unCodes) {
        Csv2Tabular csv2Tabular = new Csv2Tabular();
        TabularSeries tabularSeries = csv2Tabular.parseFile2TabularSeries("src/test/resources/FishstatJOutput.csv");
        List<org.fao.fi.tabularseries.metamodel.Concept> headerList = new ArrayList<org.fao.fi.tabularseries.metamodel.Concept>();
        String[] firstRow = tabularSeries.getTable()[0];
        // ALPHA_3_CODE,SCIENTIFIC_NAME, AREA, CATCH, YEAR
        Dimension d1 = new Dimension(firstRow[0]);
        Attribute a1 = new Attribute(firstRow[1]);
        Dimension d2 = new Dimension(firstRow[2]);
        Value v1 = new Value(firstRow[3]);
        ColumnDimension d3 = new ColumnDimension(firstRow[4]);
        headerList.add(d1);
        headerList.add(a1);
        headerList.add(d2);
        headerList.add(v1);
        headerList.add(d3);
        SeriesMetadata seriesMetadata = new SeriesMetadata(headerList);
        FishstatProcessResult r = new FishstatProcessResult();
        r.setSeriesMetadata(seriesMetadata);
        r.setTabularSeries(tabularSeries);
        return r;
    }

    public PivotTable getPivotTable() {
        FishstatProcessResult result = this.run(0, 0, null);
        TabularSeries2Pivot tabularSeries2Pivot = new TabularSeries2Pivot(result.getSeriesMetadata());
        return tabularSeries2Pivot.convert(result.getTabularSeries());
    }

}
