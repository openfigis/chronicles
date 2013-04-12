package org.fao.fi.pivot.manipulation;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.fao.fi.pivot.calculation.CalculationOnPivot;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Test;

public class RemoveOtherTest {

    @Test
    public void testExecute() {
        int rows = 10;
        int columns = 3;
        int top = 3;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        assertEquals(rows, pivotTable.getRowSection().getRowList().size());
        CalculationOnPivot c = new CalculationOnPivot();
        c.selectHorizontalTop(pivotTable, top);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        RemoveOther.execute(pivotTable);
        assertEquals(top, pivotTable.getRowSection().getRowList().size());

        List<ColumnField> cs = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : cs) {
            assertEquals(top, columnField.getFactList().size());
        }

    }

}
