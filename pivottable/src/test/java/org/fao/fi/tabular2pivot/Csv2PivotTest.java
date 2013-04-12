package org.fao.fi.tabular2pivot;

import static org.junit.Assert.assertEquals;

import org.fao.fi.pivot.model.PivotTable;
import org.junit.Test;

public class Csv2PivotTest {

    @Test
    public void testCreatePivotTableFromCsv() {
        PivotTable pivotTable = Csv2Pivot.createPivotTableFromCsv("src/test/resources/AestheticProcessTestShort.csv");
        assertEquals(12, pivotTable.getColumnSection().getColumnFieldList().size());
        assertEquals(7, pivotTable.getRowSection().getRowList().size());
        assertEquals("YEAR", pivotTable.getRowSection().getRowHeaderFieldList().get(0).getConceptName());

    }

}
