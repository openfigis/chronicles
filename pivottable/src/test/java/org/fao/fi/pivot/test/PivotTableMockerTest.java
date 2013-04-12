package org.fao.fi.pivot.test;

import static org.junit.Assert.assertTrue;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.junit.Test;

public class PivotTableMockerTest {

    PivotTableValidator v = new PivotTableValidator();

    @Test
    public void testMockIt() {
        int rows = 2;
        int columns = 3;
        int rowColumns = 2;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, rowColumns);
        assertTrue(v.validate(pivotTable));
    }

}
