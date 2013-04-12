package org.fao.fi.chronicles.calcuation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.fao.fi.chronicles.fishstatj.FishstatProcess;
import org.fao.fi.chronicles.fishstatj.FishstatProcessResult;
import org.fao.fi.pivot.calculation.CalculationOnPivot;
import org.fao.fi.pivot.calculation.CalculationType;
import org.fao.fi.pivot.model.CalculatedCollection;
import org.fao.fi.pivot.model.CalculatedField;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.RowField;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.tabular2pivot.Pivot2Csv;
import org.fao.fi.tabular2pivot.TabularSeries2Pivot;
import org.fao.fi.tabularseries.metamodel.SeriesMetadata;
import org.junit.Before;
import org.junit.Test;

public class CalculationProcess1Test {

    CalculationProcess1 calculationProcess1 = new CalculationProcess1();
    PivotTable pivotTable;
    SeriesMetadata smd;
    FishstatProcess fishstatProcessTestHelper;

    @Before
    public void before() {
        FishstatProcess f = new FishstatProcessTestHelper();
        FishstatProcessResult result = f.run(0, 0, null);
        // convert tabular series data to a pivot table
        smd = result.getSeriesMetadata();
        TabularSeries2Pivot tabularSeries2Pivot = new TabularSeries2Pivot(result.getSeriesMetadata());
        pivotTable = tabularSeries2Pivot.convert(result.getTabularSeries());
    }

    @Test
    public void testPerformCalculations() {
        assertEquals(smd.getNonColumnDimensionsIndices().length, pivotTable.getRowSection().getRowHeaderFieldList()
                .size());
        calculationProcess1.run(pivotTable, 200);

        // check whether the averages are there
        assertEquals(
                pivotTable.getColumnSection().getCalculatedCollectionList().get(0).getCalculatedFieldList().size(),
                pivotTable.getRowSection().getRowList().size());
        assertEquals(CalculationType.AVG, pivotTable.getColumnSection().getCalculatedCollectionList().get(0)
                .getCalculationType());

        // check whether the totals are there
        assertEquals(pivotTable.getRowSection().getCalculatedCollectionList().get(0).getCalculatedFieldList().size(),
                pivotTable.getColumnSection().getColumnFieldList().size());
        assertEquals(CalculationType.SUM, pivotTable.getRowSection().getCalculatedCollectionList().get(0)
                .getCalculationType());

        // check whether other and total 200 are there
        assertEquals(201, pivotTable.getRowSection().getRowList().size());
        int lastElement = pivotTable.getRowSection().getRowList().size() - 1;
        List<RowField> list = pivotTable.getRowSection().getRowList().get(lastElement).getRowFieldList();
        for (RowField rowField : list) {
            assertTrue(rowField.getValue().startsWith(CalculationOnPivot.OTHER));
        }

        // check whether the avg are sorted correctly
        CalculatedCollection avgList = pivotTable.getColumnSection().getCalculatedCollectionList().get(0);
        List<CalculatedField> fieldList = avgList.getCalculatedFieldList();
        for (int i = 1; i < fieldList.size() - 1; i++) {
            assertTrue(fieldList.get(i - 1).getValue() >= fieldList.get(i).getValue());
        }
        List<AbstractFact> factList = pivotTable.getRowSection().getRowList().get(0).getFactList();
        CalculatedField cf = calculateField(factList, CalculationType.AVG);
        assertEquals(fieldList.get(0).getValue(), cf.getValue(), 0);

        // check whether the values do contain NaN values (which they shouldn't)
        List<ColumnField> cl = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : cl) {
            List<AbstractFact> facts = columnField.getFactList();
            for (AbstractFact fact : facts) {
                assertNotSame(Double.NaN, ((Fact) fact).getValue());
            }
        }

        Pivot2Csv.write2csv(pivotTable, "CalculationProcess1Test.csv");
    }

    public CalculationProcess1 getCalculationProcess1() {
        return calculationProcess1;
    }

    public PivotTable getPivotTable() {
        return pivotTable;
    }

    public SeriesMetadata getSmd() {
        return smd;
    }

    private CalculatedField calculateField(List<AbstractFact> factList, CalculationType calculationType) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (AbstractFact fact : factList) {
            if (fact != null) {
                stats.addValue(((Fact) fact).getValue());
            }
        }
        CalculatedField acf = new CalculatedField();
        double calculationResult = 0;
        if (calculationType.equals(CalculationType.AVG)) {
            calculationResult = stats.getMean();
        }
        if (calculationType.equals(CalculationType.SUM)) {
            calculationResult = stats.getSum();
        }
        acf.setValue(calculationResult);
        return acf;
    }

}
