package org.fao.fi.tabular2pivot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.fao.fi.pivot.PivotTableException;

/**
 * Write tabular data to a csv file. Each field is stored, surrounded with double quotes because for instance scientific
 * names can have commas. The commas would confuse the csv file interpretation and therefore these values are double
 * quoted.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class Tabular2Csv {

    private Tabular2Csv() {
        // Utility classes should not have a public or default constructor
    }

    public final static void write2csv(String[][] tabularData, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileName));
            write2OutputStream(tabularData, fos);
        } catch (IOException e) {
            throw new PivotTableException(e);
        }
    }

    public final static void write2OutputStream(String[][] tabularData, OutputStream outputStream) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(outputStream);
            for (String[] row : tabularData) {
                String printRow = "";
                for (String field : row) {
                    // print an empty string instead of null
                    if (field == null) {
                        field = "";
                    }
                    printRow = printRow + '"' + field + '"' + ',';
                }
                out.write(printRow.substring(0, printRow.length() - 1));
                out.write('\n');
            }
            out.close();
        } catch (IOException e) {
            throw new PivotTableException(e);
        }
    }

}
