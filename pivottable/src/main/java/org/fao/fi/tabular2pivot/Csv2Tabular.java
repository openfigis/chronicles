package org.fao.fi.tabular2pivot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.fao.fi.pivot.PivotTableException;
import org.fao.fi.tabulardata.model.SimpleTabularPivotTable;
import org.fao.fi.tabulardata.model.TabularData;
import org.fao.fi.tabulardata.model.TabularSeries;

import com.Ostermiller.util.CSVParse;
import com.Ostermiller.util.CSVParser;

/**
 * Process of importing the csv file.
 * 
 * @author Erik van Ingen
 * 
 */
public class Csv2Tabular {

    public TabularData parseFile2TabularData(String inputFileName) {
        TabularData tabularData = new TabularData();
        tabularData.setTable(parseFile(inputFileName));
        return tabularData;
    }

    public SimpleTabularPivotTable parseFile2SimpleTabularPivotTable(String inputFileName) {
        SimpleTabularPivotTable simpleTabularPivotTable = new SimpleTabularPivotTable();
        simpleTabularPivotTable.setTable(parseFile(inputFileName));
        return simpleTabularPivotTable;
    }

    public TabularSeries parseFile2TabularSeries(String inputFileName) {
        TabularSeries series = new TabularSeries();
        series.setTable(parseFile(inputFileName));
        return series;

    }

    private String[][] parseFile(String inputFileName) {
        try {
            InputStream is = new FileInputStream(new File(inputFileName));
            CSVParse csvParser = new CSVParser(is);
            String[][] table = csvParser.getAllValues();
            return table;
        } catch (FileNotFoundException e) {
            throw new PivotTableException(e);
        } catch (IOException e) {
            throw new PivotTableException(e);
        }
    };

}
