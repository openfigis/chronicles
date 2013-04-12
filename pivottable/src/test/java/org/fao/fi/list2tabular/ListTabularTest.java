package org.fao.fi.list2tabular;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.tabularseries.metamodel.ColumnDimension;
import org.fao.fi.tabularseries.metamodel.Dimension;
import org.fao.fi.tabularseries.metamodel.SeriesMetadata;
import org.fao.fi.tabularseries.metamodel.Value;
import org.junit.Test;

public class ListTabularTest {

    String value = "kip";
    int rows = 10;
    int columns = 20;

    @Test
    public void testListList2TabularSeries() {
        List<List<String>> table = new ArrayList<List<String>>();
        for (int i = 0; i < rows; i++) {
            List<String> row = new ArrayList<String>();
            for (int j = 0; j < columns; j++) {
                row.add(value + j + i);
            }
            table.add(row);
        }

        String[][] tableNew = ListTabular.ListList2TabularSeries(table, defineSeriesMetadata());
        assertEquals(tableNew.length, rows + 1);
        boolean firstRowPassed = false;
        for (String[] row : tableNew) {
            assertEquals(row.length, columns);
            if (firstRowPassed) {
                for (String field : row) {
                    assertTrue(field.startsWith(value));
                }
            }
            firstRowPassed = true;
        }
    }

    private SeriesMetadata defineSeriesMetadata() {
        List<org.fao.fi.tabularseries.metamodel.Concept> headerList = new ArrayList<org.fao.fi.tabularseries.metamodel.Concept>();
        Dimension d1 = new Dimension("ALPHA_3_CODE");
        Dimension d2 = new Dimension("AREA");
        ColumnDimension d3 = new ColumnDimension("YEAR");
        Value v1 = new Value("CATCH");
        headerList.add(d1);
        headerList.add(d2);
        for (int i = 0; i < (columns - 3); i++) {
            headerList.add(d3);
        }
        headerList.add(v1);
        SeriesMetadata smd = new SeriesMetadata(headerList);
        return smd;
    }

}
