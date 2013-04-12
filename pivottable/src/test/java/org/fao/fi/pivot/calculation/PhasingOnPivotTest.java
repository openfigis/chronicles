package org.fao.fi.pivot.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.logic.Phase;
import org.fao.fi.pivot.manipulation.Transpose;
import org.fao.fi.pivot.model.Attribute;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.fao.fi.tabular2pivot.Csv2Pivot;
import org.junit.Test;

public class PhasingOnPivotTest {

    String[] columnNames = { Phase.U.toString(), Phase.D.toString(), Phase.M.toString(), Phase.S.toString(),
            Phase.R.toString() };

    PivotTableValidator v = new PivotTableValidator();

    /**
     * 
     * ----c1 c2 c3 c4
     * 
     * 2001 D U S R
     * 
     * 2002 M D U S
     * 
     * 2003 R M D U
     * 
     * 2004 S R M D
     * 
     * turns into
     * 
     * ---- h1 h2 h3 h4
     * 
     * per1 25 15 5 35 20
     * 
     * per2 20 35 25 5 15
     * 
     * because number of clusters per phase is this
     * 
     * ----c1 c2 c3 c4
     * 
     * -----1 2 3 4
     * 
     * 
     * 
     * 
     */
    @Test
    public void testCalculate1() {
        int rows = 4;// the years
        int columns = 4; // resources
        int rowColumns = 1;
        int nrOfPeriods = 2;
        int nrOfPhases = columnNames.length;
        PivotTable pivotTable = PivotTableMocker.mockItString(rows, columns, rowColumns);
        Transpose t = new Transpose();
        t.transpose(pivotTable, 0);

        // set number of resources per column
        for (int i = 0; i < pivotTable.getColumnSection().getColumnFieldList().size(); i++) {
            List<Attribute> list = new ArrayList<Attribute>();
            Attribute a = new Attribute(i + 1);
            list.add(a);
            pivotTable.getColumnSection().getColumnFieldList().get(i).setAttributeList(list);
        }

        // Pivot2Sysout.pivot2SysOut(pivotTable);
        PhasingOnPivot.calculate(pivotTable, nrOfPeriods, columnNames);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        assertTrue(v.validate(pivotTable));

        // Pivot2Sysout.pivot2SysOut(pivotTable);
        assertEquals(nrOfPhases, pivotTable.getColumnSection().getColumnFieldList().size());
        assertEquals(nrOfPeriods, pivotTable.getRowSection().getRowList().size());

        List<Row> rowList = pivotTable.getRowSection().getRowList();
        // row 1
        assertEquals(5, ((Fact) rowList.get(0).getFactList().get(0)).getValue(), 0);
        assertEquals(3, ((Fact) rowList.get(0).getFactList().get(1)).getValue(), 0);
        assertEquals(1, ((Fact) rowList.get(0).getFactList().get(2)).getValue(), 0);
        assertEquals(7, ((Fact) rowList.get(0).getFactList().get(3)).getValue(), 0);
        assertEquals(4, ((Fact) rowList.get(0).getFactList().get(4)).getValue(), 0);

        // row 2
        assertEquals(4, ((Fact) rowList.get(1).getFactList().get(0)).getValue(), 0);
        assertEquals(7, ((Fact) rowList.get(1).getFactList().get(1)).getValue(), 0);
        assertEquals(5, ((Fact) rowList.get(1).getFactList().get(2)).getValue(), 0);
        assertEquals(1, ((Fact) rowList.get(1).getFactList().get(3)).getValue(), 0);
        assertEquals(3, ((Fact) rowList.get(1).getFactList().get(4)).getValue(), 0);
    }

    @Test
    public void testCalculateMass() {

        int rowColumns = 1;
        int nrOfPeriods = 2;
        for (int rows = 20; rows < 50; rows++) {
            for (int columns = 3; columns < 50; columns++) {

                PivotTable pivotTable = PivotTableMocker.mockItString(rows, columns, rowColumns);
                Transpose t = new Transpose();
                t.transpose(pivotTable, 0);

                // set number of resources per column
                for (int i = 0; i < pivotTable.getColumnSection().getColumnFieldList().size(); i++) {
                    List<Attribute> list = new ArrayList<Attribute>();
                    Attribute a = new Attribute(i + 1);
                    list.add(a);
                    pivotTable.getColumnSection().getColumnFieldList().get(i).setAttributeList(list);
                }

                // Pivot2Sysout.pivot2SysOut(pivotTable);
                PhasingOnPivot.calculate(pivotTable, nrOfPeriods, columnNames);
                assertTrue(v.validate(pivotTable));
            }
        }
    }

    @Test
    public void testCalculateFromRealCsv() {
        String[] columnNames = { Phase.U.toString(), Phase.D.toString(), Phase.M.toString(), Phase.S.toString(),
                Phase.R.toString() };
        int ROWS = 11;

        PivotTable pivotTable = Csv2Pivot.createPivotTableFromCsv("src/test/resources/PhasingOnPivot.csv");

        // Reading a pivottable from a file does not get you a number of resources per column
        for (int i = 0; i < pivotTable.getColumnSection().getColumnFieldList().size(); i++) {
            List<Attribute> list = new ArrayList<Attribute>();
            Attribute a = new Attribute(i + 1);
            list.add(a);
            pivotTable.getColumnSection().getColumnFieldList().get(i).setAttributeList(list);
        }

        int initialRows = pivotTable.getRowSection().getRowList().size();
        PhasingOnPivot.calculate(pivotTable, ROWS, columnNames);
        assertEquals(initialRows, pivotTable.getRowSection().getRowList().size());
        assertEquals(columnNames.length, pivotTable.getColumnSection().getColumnFieldList().size());

    }

    
    
    @Test
    public void testCalculate0Proof() {
        int rows = 0;// the years
        int columns = 0; // resources
        int rowColumns = 1;
        int nrOfPeriods = 2;
        PivotTable pivotTable = PivotTableMocker.mockItString(rows, columns, rowColumns);
        Transpose t = new Transpose();
        t.transpose(pivotTable, 0);

        // set number of resources per column
        for (int i = 0; i < pivotTable.getColumnSection().getColumnFieldList().size(); i++) {
            List<Attribute> list = new ArrayList<Attribute>();
            Attribute a = new Attribute(i + 1);
            list.add(a);
            pivotTable.getColumnSection().getColumnFieldList().get(i).setAttributeList(list);
        }

        // Pivot2Sysout.pivot2SysOut(pivotTable);
        PhasingOnPivot.calculate(pivotTable, nrOfPeriods, columnNames);
    }
    
    
    
}
