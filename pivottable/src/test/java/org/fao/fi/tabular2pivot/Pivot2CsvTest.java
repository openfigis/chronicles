package org.fao.fi.tabular2pivot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Before;
import org.junit.Test;

public class Pivot2CsvTest {

    File file;
    String piet;

    @Before
    public void before() {
        piet = "piet.csv";
        file = new File(piet);
    }

    @Test
    public void testWrite2csv() {
        PivotTable pivotTable = PivotTableMocker.mockIt(2, 3, 3);
        Pivot2Csv.write2csv(pivotTable, piet);
        file.delete();

    }

    @Test
    public void testWrite2OutputStream() throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(file);
        PivotTable pivotTable = PivotTableMocker.mockIt(2, 3, 3);
        Pivot2Csv.write2OutputStream(pivotTable, fos);
        file.delete();
    }
}
