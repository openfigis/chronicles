package org.fao.fi.pivot.calculation;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Test;

public class SortOnPivotTest {

    SortOnPivot sortOnPivot = new SortOnPivot();

    /**
     * fact date
     * 
     * row 2000 2001 2002
     * 
     * row1 1 2 3
     * 
     * row2 4 5 6
     */
    @Test
    public void testSortHighToLowOnAverage() {
        PivotTable pivotTable = CalculationOnPivotTest.createPivotTableMock();
        sortOnPivot.sortHighToLowOnAverage(pivotTable);
        List<Row> rowList = pivotTable.getRowSection().getRowList();
        assertEquals(4.0, ((Fact) rowList.get(0).getFactList().get(0)).getValue(), 0);
        assertEquals(3.0, ((Fact) rowList.get(1).getFactList().get(2)).getValue(), 0);

        assertEquals(5.0, pivotTable.getColumnSection().getCalculatedCollectionList().get(0).getCalculatedFieldList()
                .get(0).getValue(), 0);
        assertEquals(2.0, pivotTable.getColumnSection().getCalculatedCollectionList().get(0).getCalculatedFieldList()
                .get(1).getValue(), 0);
    }

    /**
     * fact date
     * 
     * row 2000 2001 2002
     * 
     * row1 1 2 3
     * 
     * row2 4 5 6
     */
    @Test
    public void testSortHighToLowOnAverage2() {
        PivotTable pivotTable = PivotTableMocker.mockIt(99, 5, 2);

        CalculationOnPivot calculationOnPivot = new CalculationOnPivot();
        calculationOnPivot.doHorizontalCalculationOn(pivotTable, CalculationType.AVG);

        Pivot2Sysout.pivot2SysOut(pivotTable);

        sortOnPivot.sortHighToLowOnAverage(pivotTable);
        Pivot2Sysout.pivot2SysOut(pivotTable);
    }

    @Test
    public void testTranspose() {
        int rows = 3;
        int columns = 3;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);
        CalculationOnPivot calculationOnPivot = new CalculationOnPivot();
        calculationOnPivot.doHorizontalCalculationOn(pivotTable, CalculationType.AVG);
        SortOnPivot sortOnPivot = new SortOnPivot();
        Pivot2Sysout.pivot2SysOut(pivotTable);
        sortOnPivot.sortHighToLowOnAverage(pivotTable);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        assertEquals(7.0, ((Fact) pivotTable.getRowSection().getRowList().get(0).getFactList().get(0)).getValue(), 0);
        assertEquals(7.0,
                ((Fact) pivotTable.getColumnSection().getColumnFieldList().get(0).getFactList().get(0)).getValue(), 0);

    }

}
