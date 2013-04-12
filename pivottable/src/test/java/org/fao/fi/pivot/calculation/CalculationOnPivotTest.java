package org.fao.fi.pivot.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.fao.fi.pivot.model.CalculatedCollection;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.Derived;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Test;

public class CalculationOnPivotTest {

    CalculationOnPivot calculationOnPivot = new CalculationOnPivot();
    PivotTableValidator v = new PivotTableValidator();

    @Test
    public void testDoVerticalAverageOn() {
        PivotTable pt = createPivotTableMock();
        calculationOnPivot.doVerticalCalculationOn(pt, CalculationType.AVG);
        List<CalculatedCollection> alist = pt.getRowSection().getCalculatedCollectionList();
        assertEquals(alist.size(), 1);
        assertEquals(alist.get(0).getCalculatedFieldList().get(0).getValue(), 2.5, 0);
        assertEquals(alist.get(0).getCalculatedFieldList().get(1).getValue(), 3.5, 0);
        assertEquals(alist.get(0).getCalculatedFieldList().get(2).getValue(), 4.5, 0);
        assertEquals(alist.get(0).getCalculationType(), CalculationType.AVG);

    }

    @Test
    public void testDoHorizontalAverageOn() {
        PivotTable pt = createPivotTableMock();
        calculationOnPivot.doHorizontalCalculationOn(pt, CalculationType.AVG);
        List<CalculatedCollection> alist = pt.getColumnSection().getCalculatedCollectionList();
        assertEquals(1, alist.size());
        assertEquals(pt.getRowSection().getRowList().size(), alist.get(0).getCalculatedFieldList().size());
        assertEquals(alist.get(0).getCalculatedFieldList().get(0).getValue(), 2.0, 0);
        assertEquals(alist.get(0).getCalculatedFieldList().get(1).getValue(), 5.0, 0);
        assertEquals(alist.get(0).getCalculationType(), CalculationType.AVG);

    }

    @Test
    public void testDoVerticalSumOn() {
        PivotTable pt = createPivotTableMock();
        calculationOnPivot.doVerticalCalculationOn(pt, CalculationType.SUM);
        List<CalculatedCollection> alist = pt.getRowSection().getCalculatedCollectionList();
        assertEquals(alist.size(), 1);
        assertEquals(alist.get(0).getCalculatedFieldList().get(0).getValue(), 5.0, 0);
        assertEquals(alist.get(0).getCalculatedFieldList().get(1).getValue(), 7.0, 0);
        assertEquals(alist.get(0).getCalculatedFieldList().get(2).getValue(), 9.0, 0);
        assertEquals(alist.get(0).getCalculationType(), CalculationType.SUM);

    }

    @Test
    public void testDoHorizontalSumOn() {
        PivotTable pt = createPivotTableMock();
        calculationOnPivot.doHorizontalCalculationOn(pt, CalculationType.SUM);
        List<CalculatedCollection> alist = pt.getColumnSection().getCalculatedCollectionList();
        assertEquals(1, alist.size());
        assertEquals(pt.getRowSection().getRowList().size(), alist.get(0).getCalculatedFieldList().size());
        assertEquals(alist.get(0).getCalculatedFieldList().get(0).getValue(), 6.0, 0);
        assertEquals(alist.get(0).getCalculatedFieldList().get(1).getValue(), 15.0, 0);
        assertEquals(alist.get(0).getCalculationType(), CalculationType.SUM);

    }

    /**
     * row1 1 2 3
     * 
     * OTHER 4 5 6
     * 
     */
    @Test
    public void testSelectHorizontalTop1() {
        PivotTable pt = PivotTableMocker.mockIt(3, 3, 3);
        calculationOnPivot.doHorizontalCalculationOn(pt, CalculationType.AVG);
        int numberOfSelectedItems = 1;

        Pivot2Sysout.pivot2SysOut(pt);

        calculationOnPivot.selectHorizontalTop(pt, numberOfSelectedItems);

        Pivot2Sysout.pivot2SysOut(pt);

        List<Row> alist = pt.getRowSection().getRowList();
        assertEquals(2, alist.size());
        assertEquals(pt.getColumnSection().getColumnFieldList().size(), alist.get(0).getFactList().size());
        assertEquals(pt.getColumnSection().getColumnFieldList().size(), alist.get(1).getFactList().size());
        assertEquals(((Fact) alist.get(0).getFactList().get(0)).getValue(), 1.0, 0);
        assertEquals(((Fact) alist.get(0).getFactList().get(1)).getValue(), 2.0, 0);
        assertEquals(((Fact) alist.get(0).getFactList().get(2)).getValue(), 3.0, 0);
        assertEquals(((Fact) alist.get(1).getFactList().get(0)).getValue(), 11.0, 0);
        assertEquals(((Fact) alist.get(1).getFactList().get(1)).getValue(), 13.0, 0);
        assertEquals(((Fact) alist.get(1).getFactList().get(2)).getValue(), 15.0, 0);

        Fact factFromRow = (Fact) alist.get(1).getFactList().get(0);
        Fact factFromColumn = (Fact) pt.getColumnSection().getColumnFieldList().get(0).getFactList().get(1);
        assertEquals(factFromRow.getValue(), factFromColumn.getValue(), 0);
        assertEquals(factFromRow, factFromColumn);

        assertEquals("row1", alist.get(0).getRowFieldList().get(0).getValue());
        assertEquals("OTHER", alist.get(1).getRowFieldList().get(0).getValue());
        assertTrue(alist.get(1) instanceof Derived);

        v.validate(pt);

    }

    @Test
    public void testSelectHorizontalTop2() {
        PivotTableValidator v = new PivotTableValidator();
        int rows = 4;
        int columns = 1;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);
        CalculationOnPivot c = new CalculationOnPivot();
        c.selectHorizontalTop(pivotTable, 1);
        assertTrue(v.validate(pivotTable));
    }

    /**
     * When the selected top is larger than the available
     */
    @Test
    public void testSelectHorizontalTop3() {
        PivotTableValidator v = new PivotTableValidator();
        int rows = 4;
        int columns = 1;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);
        CalculationOnPivot c = new CalculationOnPivot();
        c.selectHorizontalTop(pivotTable, 5);
        assertTrue(v.validate(pivotTable));
    }

    /**
     * Fact year null
     * 
     * dName1 2001 2002
     * 
     * row1 1.0 2.0
     * 
     * row2 3.0 4.0
     * 
     * row3 5.0 6.0
     * 
     * INTO
     * 
     * -1 -1
     * 
     * 0 0
     * 
     * 1 1
     */
    @Test
    public void testStandardizeVerticalValues1() {
        int rows = 3;
        int columns = 2;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        CalculationOnPivot c = new CalculationOnPivot();
        c.standardizeVerticalValues(pivotTable);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        assertEquals(-1, ((Fact) pivotTable.getRowSection().getRowList().get(0).getFactList().get(0)).getValue(), 0);
        assertEquals(-1, ((Fact) pivotTable.getRowSection().getRowList().get(0).getFactList().get(1)).getValue(), 0);
        assertEquals(0, ((Fact) pivotTable.getRowSection().getRowList().get(1).getFactList().get(0)).getValue(), 0);
        assertEquals(0, ((Fact) pivotTable.getRowSection().getRowList().get(1).getFactList().get(1)).getValue(), 0);
        assertEquals(1, ((Fact) pivotTable.getRowSection().getRowList().get(2).getFactList().get(0)).getValue(), 0);
        assertEquals(1, ((Fact) pivotTable.getRowSection().getRowList().get(2).getFactList().get(1)).getValue(), 0);
    }

    /**
     * check transfer of row column fields
     */
    @Test
    public void testStandardizeVerticalValues2() {
        int rows = 21;
        int columns = 29;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 3);
        String[] rowHeaderFields = new String[pivotTable.getRowSection().getRowHeaderFieldList().size()];
        for (int i = 0; i < rowHeaderFields.length; i++) {
            rowHeaderFields[i] = pivotTable.getRowSection().getRowHeaderFieldList().get(i).getConceptName();
        }
        String columnSectionConcept = pivotTable.getColumnSection().getConceptName();

        Pivot2Sysout.pivot2SysOut(pivotTable);
        CalculationOnPivot c = new CalculationOnPivot();
        c.standardizeVerticalValues(pivotTable);
        Pivot2Sysout.pivot2SysOut(pivotTable);

        for (int i = 0; i < rowHeaderFields.length; i++) {
            assertEquals(rowHeaderFields[i], pivotTable.getRowSection().getRowHeaderFieldList().get(i).getConceptName());
        }

        assertEquals(columnSectionConcept, pivotTable.getColumnSection().getConceptName());

    }

    /**
     * test on standardization of a sample of all 0 values.
     */
    @Test
    public void testStandardizeVerticalValuesNan() {
        int rows = 10;
        int columns = 10;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 3);
        CalculationOnPivot c = new CalculationOnPivot();
        List<ColumnField> list = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : list) {
            List<AbstractFact> fl = columnField.getFactList();
            for (AbstractFact abstractFact : fl) {
                Fact fact = (Fact) abstractFact;
                fact.setValue(0.0);
            }
        }

        c.standardizeVerticalValues(pivotTable);
        list = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : list) {
            List<AbstractFact> fl = columnField.getFactList();
            for (AbstractFact abstractFact : fl) {
                Fact fact = (Fact) abstractFact;
                assertFalse(Double.isNaN(fact.getValue()));
            }
        }

    }

    @Test
    public void testSummarizeTop() {
        int rows = 4;
        int columns = 3;
        int numberOfSelectedItems = 2;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 3);
        calculationOnPivot.doHorizontalCalculationOn(pivotTable, CalculationType.AVG);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        calculationOnPivot.selectHorizontalTop(pivotTable, numberOfSelectedItems);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        calculationOnPivot.summarizeTop(pivotTable);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        assertEquals(2, pivotTable.getRowSection().getRowList().size());
        v.validate(pivotTable);
        System.out.println(v.getErrorString());

        assertTrue(v.validate(pivotTable));
        System.out.println(v.getErrorString());
        

        assertEquals(5, ((Fact) pivotTable.getRowSection().getRowList().get(0).getFactList().get(0)).getValue(), 0);
        assertEquals(17, ((Fact) pivotTable.getRowSection().getRowList().get(1).getFactList().get(0)).getValue(), 0);

        assertEquals(CalculationOnPivot.TOP, pivotTable.getRowSection().getRowList().get(0).getRowFieldList().get(0)
                .getValue());
        assertEquals(CalculationOnPivot.OTHER, pivotTable.getRowSection().getRowList().get(1).getRowFieldList().get(0)
                .getValue());
        assertEquals(CalculationOnPivot.AGGREGATION, pivotTable.getRowSection().getRowHeaderFieldList().get(0)
                .getConceptName());

    }

    /**
     * The chronicles application has this bug:
     * http://localhost:8080/chronicles-web/chart/capture/startyear/1950/endyear/2007/top/200/un/036/chart.png This
     * should give the chart for Australia, but it gives this message: org.fao.fi.pivot.PivotTableException: Not
     * correct, the last row should contain the record with the summarised other values, perform a selectHorizontalTop
     * first org.fao.fi.pivot.calculation.CalculationOnPivot.summarizeTop(CalculationOnPivot.java:241)
     * 
     * 
     */
    @Test
    public void testSummarizeTopBug1() {
        int rows = 0;
        int columns = 3;
        int numberOfSelectedItems = 6;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 3);
        calculationOnPivot.doHorizontalCalculationOn(pivotTable, CalculationType.AVG);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        calculationOnPivot.selectHorizontalTop(pivotTable, numberOfSelectedItems);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        calculationOnPivot.summarizeTop(pivotTable);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        assertEquals(2, pivotTable.getRowSection().getRowList().size());
        assertTrue(v.validate(pivotTable));
    }

    /**
     * The chronicles application has this bug: java.lang.IllegalArgumentException: fromIndex(0) > toIndex(-1)
     * java.util.SubList.<init>(AbstractList.java:604) java.util.RandomAccessSubList.<init>(AbstractList.java:758)
     * java.util.AbstractList.subList(AbstractList.java:468)
     * org.fao.fi.pivot.calculation.CalculationOnPivot.summarizeTop(CalculationOnPivot.java:239)
     * org.fao.fi.chronicles.calcuation.CatchCalculationProcess2.sumUpTotals(CatchCalculationProcess2.java:19)
     * 
     * 
     */
    @Test
    public void testSummarizeTopBug2() {
        int rows = 0;
        int columns = 3;
        int numberOfSelectedItems = 5;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 3);
        calculationOnPivot.doHorizontalCalculationOn(pivotTable, CalculationType.AVG);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        calculationOnPivot.selectHorizontalTop(pivotTable, numberOfSelectedItems);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        calculationOnPivot.summarizeTop(pivotTable);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        assertEquals(2, pivotTable.getRowSection().getRowList().size());
        assertTrue(v.validate(pivotTable));
    }

    /**
     * fact date
     * 
     * row 2000 2001 2002
     * 
     * row1 1 2 3
     * 
     * row2 4 5 6
     * 
     * 
     * @return
     */

    public static final PivotTable createPivotTableMock() {
        return PivotTableMocker.mockIt(2, 3, 3);
    }

}
