package org.fao.fi.tabular2pivot;

import static org.junit.Assert.assertEquals;

import org.fao.fi.tabulardata.model.SimpleTabularPivotTable;
import org.fao.fi.tabulardata.model.TabularSeries;
import org.junit.Test;

public class Csv2TabularTest {

    @Test
    public void testParseFile2SimpleTabularPivotTable() {
        String inputFileName = "src/test/resources/EXPORT.CSV";
        Csv2Tabular p = new Csv2Tabular();
        SimpleTabularPivotTable toppy = p.parseFile2SimpleTabularPivotTable(inputFileName);
        String[][] table = toppy.getTable();
        assertEquals(12, table.length);
        assertEquals(61, table[0].length);

    }

    @Test
    public void testImportTop200() {
        String inputFileName = "src/test/resources/FishstatJOutput.csv";
        Csv2Tabular p = new Csv2Tabular();
        TabularSeries toppy = p.parseFile2TabularSeries(inputFileName);
        String[][] table = toppy.getTable();
        assertEquals(2963, table.length);
        for (String[] row : table) {
            assertEquals(5, row.length);
        }

    }

}
