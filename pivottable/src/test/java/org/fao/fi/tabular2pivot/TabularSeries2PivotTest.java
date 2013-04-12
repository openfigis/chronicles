package org.fao.fi.tabular2pivot;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.RowHeaderField;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.tabulardata.model.TabularSeries;
import org.fao.fi.tabularseries.metamodel.Attribute;
import org.fao.fi.tabularseries.metamodel.ColumnDimension;
import org.fao.fi.tabularseries.metamodel.Concept;
import org.fao.fi.tabularseries.metamodel.Dimension;
import org.fao.fi.tabularseries.metamodel.SeriesMetadata;
import org.fao.fi.tabularseries.metamodel.Value;
import org.junit.Test;

public class TabularSeries2PivotTest {

    @Test
    public void testConvert1() {
        TabularSeries2Pivot converter = new TabularSeries2Pivot(getMapping());
        PivotTable pt = converter.convert(getSeries());
        List<ColumnField> columnFieldList = pt.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : columnFieldList) {
            System.out.println(pt.getRowSection().getRowList().size() + " " + columnField.getFactList().size());
            assertEquals(pt.getRowSection().getRowList().size(), columnField.getFactList().size());
        }

        List<Row> rowList = pt.getRowSection().getRowList();
        for (Row row : rowList) {
            System.out.println(row.getFactList().size() + " " + pt.getColumnSection().getColumnFieldList().size());
            assertEquals(pt.getColumnSection().getColumnFieldList().size(), row.getFactList().size());
        }

        List<RowHeaderField> rowHeaderFieldList = pt.getRowSection().getRowHeaderFieldList();
        assertEquals(3, columnFieldList.size());
        assertEquals(3, rowList.size());
        assertEquals(1, rowHeaderFieldList.size());

        // checking the content of the column section
        ColumnField columnField = columnFieldList.get(0);
        assertEquals("2000", columnField.getValue());
        assertEquals(7, ((Fact) columnField.getFactList().get(0)).getValue(), 0);

        // checking the content of the row section
        Row row1 = rowList.get(0);
        Row row2 = rowList.get(1);
        assertEquals(3, row1.getFactList().size());
        assertEquals(1, row1.getRowFieldList().size());
        assertEquals(3, row2.getFactList().size());
        assertEquals(1, row2.getRowFieldList().size());

    }

    public static final SeriesMetadata getMapping() {
        Dimension d1 = new Dimension("country");
        ColumnDimension d2 = new ColumnDimension("year");
        Attribute a1 = new Attribute("comment");
        Value v1 = new Value("catch");
        List<Concept> headerList = new ArrayList<Concept>();
        headerList.add(d1);
        headerList.add(d2);
        headerList.add(a1);
        headerList.add(v1);

        SeriesMetadata mapping = new SeriesMetadata(headerList);
        mapping.setHeaderList(headerList);
        return mapping;
    }

    public static final TabularSeries getSeries() {
        TabularSeries series = new TabularSeries();
        String table[][] = new String[7][4];
        String row1[] = { "country", "year", "comment", "catch" };
        String row2[] = { "nl", "2000", "low", "7" };
        String row3[] = { "nl", "2001", "medium", "8" };
        String row4[] = { "dl", "2002", "none", "10" };
        String row5[] = { "it", "2000", "none", "11" };
        String row6[] = { "it", "2001", "none", "12" };
        String row7[] = { "it", "2002", "none", "13" };
        table[0] = row1;
        table[1] = row2;
        table[2] = row3;
        table[3] = row4;
        table[4] = row5;
        table[5] = row6;
        table[6] = row7;
        series.setTable(table);
        return series;
    }
}
