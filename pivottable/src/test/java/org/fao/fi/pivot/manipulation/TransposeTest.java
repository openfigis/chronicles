package org.fao.fi.pivot.manipulation;

import static org.junit.Assert.assertEquals;

import org.fao.fi.pivot.calculation.CalculationOnPivot;
import org.fao.fi.pivot.calculation.CalculationType;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Test;

public class TransposeTest {

    /**
     * 
     * FactForAFact year null null
     * 
     * dimensionName1 2001 2002 2003
     * 
     * row1 1.0 2.0 3.0
     * 
     * row2 4.0 5.0 6.0
     * 
     * TO
     * 
     * FactForAFact
     * 
     * dimensionName1 year
     * 
     * row1 row2
     * 
     * 2001 1.0 4.0
     * 
     * 2001 2.0 5.0
     * 
     * 2002 3.0 6.0
     */

    @Test
    public void testTranspose() {
        int rows = 2;
        int columns = 3;
        int indexRowSectionColumn = 0;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);
        double originalValue = ((Fact) pivotTable.getRowSection().getRowList().get(1).getFactList().get(1)).getValue();

        Pivot2Sysout.pivot2SysOut(pivotTable);
        Transpose t = new Transpose();
        t.transpose(pivotTable, indexRowSectionColumn);
        String columnDimensionName = PivotTableMocker.DIMENSION_NAME + (indexRowSectionColumn + 1);
        assertEquals(columnDimensionName, pivotTable.getColumnSection().getConceptName());
        assertEquals(PivotTableMocker.COLUMN_DIMENSION_NAME, pivotTable.getRowSection().getRowHeaderFieldList().get(0)
                .getConceptName());
        assertEquals(rows, pivotTable.getColumnSection().getColumnFieldList().size());
        assertEquals(columns, pivotTable.getRowSection().getRowList().size());

        double newValue = ((Fact) pivotTable.getRowSection().getRowList().get(1).getFactList().get(1)).getValue();

        assertEquals(originalValue, newValue, 0);

        // Pivot2Sysout.pivot2SysOut(pivotTable);
    }

    @Test
    public void testTransposeWithAggregatedValues() {
        int rows = 2;
        int columns = 3;
        int indexRowSectionColumn = 0;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);
        CalculationOnPivot c = new CalculationOnPivot();
        c.doHorizontalCalculationOn(pivotTable, CalculationType.AVG);
        c.doHorizontalCalculationOn(pivotTable, CalculationType.SUM);
        c.doVerticalCalculationOn(pivotTable, CalculationType.SUM);
        assertEquals(1, pivotTable.getRowSection().getCalculatedCollectionList().size());
        assertEquals(2, pivotTable.getColumnSection().getCalculatedCollectionList().size());
        Transpose t = new Transpose();
        t.transpose(pivotTable, indexRowSectionColumn);
        assertEquals(2, pivotTable.getRowSection().getCalculatedCollectionList().size());
        assertEquals(1, pivotTable.getColumnSection().getCalculatedCollectionList().size());
    }

}
