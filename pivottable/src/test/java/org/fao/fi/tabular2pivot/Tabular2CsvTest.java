package org.fao.fi.tabular2pivot;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.Test;

public class Tabular2CsvTest {

    @Test
    public void testWrite2csv() {
        String[] a = { "a,b", "4", "6" };
        String[] b = { "100.0,b", "4", "6" };
        String[][] tabularData = { a, b };
        String fileName = "Tabular2CsvTest.csv";
        Tabular2Csv.write2csv(tabularData, fileName);
        File file = new File(fileName);
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testWrite2OutputStream() throws FileNotFoundException {
        String[] a = { "a,b", "4", "6" };
        String[] b = { "100.0,b", "4", "6" };
        String[][] tabularData = { a, b };
        String fileName = "Tabular2CsvTesttestWrite2OutputStream.csv";
        FileOutputStream fos = new FileOutputStream(new File(fileName));
        Tabular2Csv.write2OutputStream(tabularData, fos);
        File file = new File(fileName);
        assertTrue(file.exists());
        file.delete();
    }
}
