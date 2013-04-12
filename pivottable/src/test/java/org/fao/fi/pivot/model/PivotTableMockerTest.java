package org.fao.fi.pivot.model;

import static org.junit.Assert.assertEquals;

import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Test;

public class PivotTableMockerTest {

    @Test
    public void testMockIt() {
        int rows = 2;
        int columns = 3;
        PivotTable pt = PivotTableMocker.mockIt(2, 3, 3);
        assertEquals(rows, pt.getRowSection().getRowList().size());
        assertEquals(columns, pt.getColumnSection().getColumnFieldList().size());
    }

}
