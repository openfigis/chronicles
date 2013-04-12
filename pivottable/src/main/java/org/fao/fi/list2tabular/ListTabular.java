package org.fao.fi.list2tabular;

import java.util.List;

import org.fao.fi.tabularseries.metamodel.Concept;
import org.fao.fi.tabularseries.metamodel.SeriesMetadata;

public class ListTabular {

    private ListTabular() {
        // Utility classes should not have a public or default constructor
    }

    public static String[][] ListList2TabularSeries(List<List<String>> table, SeriesMetadata seriesMetadata) {
        String[][] tabularSeries = new String[table.size() + 1][seriesMetadata.getHeaderList().size()];

        // fill the first row with the column names
        List<? extends Concept> headerList = seriesMetadata.getHeaderList();
        for (int i = 0; i < headerList.size(); i++) {
            tabularSeries[0][i] = headerList.get(i).getName();
        }

        // add the data
        for (int i = 0; i < table.size(); i++) {
            List<String> row = table.get(i);
            for (int j = 0; j < row.size(); j++) {
                tabularSeries[i + 1][j] = row.get(j);
            }
        }
        return tabularSeries;
    }

}
