package org.fao.fi.tabular2pivot;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.calculation.CalculationOnPivot;
import org.fao.fi.pivot.calculation.CalculationType;
import org.fao.fi.pivot.manipulation.Transpose;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.fao.fi.tabulardata.model.TabularSeries;
import org.fao.fi.tabularseries.metamodel.Attribute;
import org.fao.fi.tabularseries.metamodel.ColumnDimension;
import org.fao.fi.tabularseries.metamodel.Concept;
import org.fao.fi.tabularseries.metamodel.Dimension;
import org.fao.fi.tabularseries.metamodel.SeriesMetadata;
import org.fao.fi.tabularseries.metamodel.Value;
import org.junit.Test;

public class Pivot2TabularTest {
    Pivot2Tabular pivot2Tabular = new Pivot2Tabular();

    @Test
    public void testPivot2TabularWith2RowFields() {
        int rows = 2;
        int columns = 4;
        int rowColumns = 2;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, rowColumns);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        String[][] table = pivot2Tabular.pivot2Tabular(pivotTable);
        int width = pivotTable.getRowSection().getRowHeaderFieldList().size()
                + pivotTable.getColumnSection().getColumnFieldList().size();
        assertEquals((columns + rowColumns), width);
        for (String[] row : table) {
            assertEquals(width, row.length);
        }
    }

    @Test
    public void testPivot2Tabular() {
        String[][] testTable = getExpectedResult();
        performValidation(testTable);
    }

    @Test
    public void testPivot2TabularAggregated1() {
        String[][] testTable = getExpectedResultWithAggregationRow();
        performValidation(testTable);
    }

    @Test
    public void testPivot2TabularHorizontalVertical() {
        // Pivot2Sysout d = new Pivot2Sysout();
        int rows = 2;
        int columns = 4;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);
        CalculationOnPivot c = new CalculationOnPivot();
        c.doHorizontalCalculationOn(pivotTable, CalculationType.SUM);
        c.doHorizontalCalculationOn(pivotTable, CalculationType.AVG);
        c.doVerticalCalculationOn(pivotTable, CalculationType.SUM);
        c.doVerticalCalculationOn(pivotTable, CalculationType.AVG);
        pivot2Tabular = new Pivot2Tabular();
        String[][] table = pivot2Tabular.pivot2Tabular(pivotTable);
        assertEquals(7, table[0].length);
        assertEquals(6, table.length);
        Transpose transpose = new Transpose();
        // d.pivot2SysOut(pivotTable);
        transpose.transpose(pivotTable, 0);
        String[][] transposedTable = pivot2Tabular.pivot2Tabular(pivotTable);
        // d.pivot2SysOut(pivotTable);
        assertEquals(5, transposedTable[0].length);
        assertEquals(8, transposedTable.length);
    }

    @Test
    public void testPivot2TabularAggregatedHorizontal() {
        String x1[] = { "catch", "year", null, null, null };
        String x2[] = { "country", "2000", "2001", "2002", CalculationType.AVG.toString() };
        String x3[] = { "nl", "7.0", "8.0", null, "7.5" };
        String x4[] = { "dl", null, null, "10.0", "10.0" };
        String x5[] = { "it", "11.0", "12.0", "13.0", "12.0" };
        String testTable[][] = { x1, x2, x3, x4, x5 };

        pivot2Tabular = new Pivot2Tabular();
        TabularSeries2Pivot converter = new TabularSeries2Pivot(TabularSeries2PivotTest.getMapping());
        PivotTable pt = converter.convert(TabularSeries2PivotTest.getSeries());
        CalculationOnPivot a = new CalculationOnPivot();
        a.doHorizontalCalculationOn(pt, CalculationType.AVG);
        String[][] table = pivot2Tabular.pivot2Tabular(pt);

        for (int i = 0; i < table.length; i++) {
            String[] row = table[i];
            for (int j = 0; j < row.length; j++) {
                String field = row[j];
                assertEquals(testTable[i][j], field);
            }
        }

    }

    @Test
    public void printPivotTabular() {
        pivot2Tabular = new Pivot2Tabular();
        TabularSeries2Pivot converter = new TabularSeries2Pivot(TabularSeries2PivotTest.getMapping());
        PivotTable pivotTable = converter.convert(TabularSeries2PivotTest.getSeries());
        String tabularPivot[][] = pivot2Tabular.pivot2Tabular(pivotTable);

        for (String[] row : tabularPivot) {
            for (String field : row) {
                System.out.print(field + '\t');
            }
            System.out.println();
        }
    }

    @Test
    public void testConvert2() {
        List<Concept> headerList = new ArrayList<Concept>();
        TabularSeries s = new TabularSeries();
        Dimension d1 = new Dimension("ALPHA_3_CODE");
        Attribute a1 = new Attribute("SCIENTIFIC_NAME");
        Dimension d2 = new Dimension("AREA");
        Value v1 = new Value("CATCH");
        ColumnDimension d3 = new ColumnDimension("YEAR");
        headerList.add(d1);
        headerList.add(a1);
        headerList.add(d2);
        headerList.add(v1);
        headerList.add(d3);
        SeriesMetadata smd = new SeriesMetadata(headerList);
        String[] firstRow = { "ALPHA_3_CODE", "SCIENTIFIC_NAME", "AREA", "CATCH", "YEAR", };
        String[] secondRow = { "MIW", "Balaenoptera acutorostrata", "98", "1", "1950" };
        String[] thirdRow = { "NAR", "Monodon monoceros", "99", "9", "1950" };
        TabularSeries2Pivot tabularSeries2Pivot = new TabularSeries2Pivot(smd);
        String tableRaw[][] = new String[][] { firstRow, secondRow, thirdRow };
        s.setTable(tableRaw);
        PivotTable pivotTable = tabularSeries2Pivot.convert(s);
        CalculationOnPivot a = new CalculationOnPivot();
        a.doVerticalCalculationOn(pivotTable, CalculationType.AVG);

        assertEquals(1, pivotTable.getColumnSection().getColumnFieldList().size());
        assertEquals(2, pivotTable.getRowSection().getRowList().size());
        assertEquals(2, pivotTable.getRowSection().getRowHeaderFieldList().size());
        assertEquals(1, pivotTable.getRowSection().getCalculatedCollectionList().size());
        assertEquals(1.0, ((Fact) pivotTable.getRowSection().getRowList().get(0).getFactList().get(0)).getValue(), 0.0);
        assertEquals(5.0,
                pivotTable.getRowSection().getCalculatedCollectionList().get(0).getCalculatedFieldList().get(0)
                        .getValue(), 0);

        pivot2Tabular = new Pivot2Tabular();
        String[][] table = pivot2Tabular.pivot2Tabular(pivotTable);

        String x1[] = { "CATCH", "YEAR", null };
        String x2[] = { "ALPHA_3_CODE", "AREA", "1950" };
        String x3[] = { "MIW", "98", "1.0" };
        String x4[] = { "NAR", "99", "9.0" };
        String x5[] = { CalculationType.AVG.toString(), null, "5.0" };
        String testTable[][] = { x1, x2, x3, x4, x5 };

        for (int i = 0; i < table.length; i++) {
            String[] row = table[i];
            for (int j = 0; j < row.length; j++) {
                String field = row[j];
                assertEquals(testTable[i][j], field);
            }
        }

    }

    private void performValidation(String[][] testTable) {
        pivot2Tabular = new Pivot2Tabular();
        TabularSeries2Pivot converter = new TabularSeries2Pivot(TabularSeries2PivotTest.getMapping());
        PivotTable pt = converter.convert(TabularSeries2PivotTest.getSeries());
        String[][] table = pivot2Tabular.pivot2Tabular(pt);

        for (int i = 0; i < table.length; i++) {
            String[] row = table[i];
            for (int j = 0; j < row.length; j++) {
                String field = row[j];
                assertEquals(testTable[i][j], field);
            }
        }
    }

    private String[][] getExpectedResult() {
        String x1[] = { "catch", "year", null, null };
        String x2[] = { "country", "2000", "2001", "2002" };
        String x3[] = { "nl", "7.0", "8.0", null };
        String x4[] = { "dl", null, null, "10.0" };
        String x5[] = { "it", "11.0", "12.0", "13.0" };
        String testTable[][] = { x1, x2, x3, x4, x5 };
        return testTable;
    }

    private String[][] getExpectedResultWithAggregationRow() {
        String[][] testTable = getExpectedResult();
        String a[] = { "AGGREGATED", "9.0", "10.0", "11.5" };
        String agtable[][] = { testTable[0], testTable[1], testTable[2], testTable[3], testTable[4], a };
        return agtable;
    }

}
