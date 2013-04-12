package org.fao.fi.pivot.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.RowField;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.model.fact.StringFact;
import org.fao.fi.pivot.util.PivotUtil;

/**
 * Use a String fact cluster PivotTable as an input. Produce the pivot table with the percentage of occurrences of a
 * certain phase per period.
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class PhasingOnPivot {

    private PhasingOnPivot() {
        // Utility classes should not have a public or default constructor
    }

    private Map<Integer, List<Integer>> rowMap = new HashMap<Integer, List<Integer>>();

    /**
     * The number of rows will be redefined to the number of rows given. So the rows will be aggregated to a new number
     * of rows.
     * 
     * 
     * 
     * @param pivotTable
     * @param rows
     * @param columnNames
     */
    public static void calculate(PivotTable pivotTable, int rows, String[] columnNames) {
        // if the new number of rows is lower than the actual number or rows, it will remain the actual number of rows.
        if (rows > pivotTable.getRowSection().getRowList().size()) {
            rows = pivotTable.getRowSection().getRowList().size();
        }
        PhasingOnPivot p = new PhasingOnPivot();
        p.generateIndexMap(pivotTable, rows);
        p.calculateColumns(pivotTable, columnNames, rows);
        PivotUtil.copyFactsFromRow2EmptyColumn(pivotTable);
        p.addCorrectColumnNames(pivotTable, columnNames);

    }

    private void addCorrectColumnNames(PivotTable pivotTable, String[] columnNames) {
        if (pivotTable.getColumnSection().getColumnFieldList().size() > 0) {
            for (int i = 0; i < columnNames.length; i++) {
                String cName = columnNames[i];
                pivotTable.getColumnSection().getColumnFieldList().get(i).setValue(cName);
            }
        }
    }

    private void calculateColumns(PivotTable pivotTable, String[] columnNames, int rows) {
        List<ColumnField> oldColumns = pivotTable.getColumnSection().getColumnFieldList();

        List<Row> oldRows = pivotTable.getRowSection().getRowList();
        List<Row> newRows = new ArrayList<Row>();

        Map<String, Integer> columnFactMap = initialiseMap(columnNames);

        // build up new map with values per each row
        for (int i = 0; i < rows; i++) {
            List<Integer> list = rowMap.get(i);
            for (Integer integer : list) {
                Row oldRow = oldRows.get(integer);
                for (ColumnField columnField : oldColumns) {
                    StringFact sf = (StringFact) oldRow.getFactList().get(oldColumns.indexOf(columnField));
                    int numberOfElements = columnField.getAttributeList().get(0).getValue();
                    Integer value = columnFactMap.get(sf.getValue());
                    int newValue = value + numberOfElements;
                    columnFactMap.put(sf.getValue(), newValue);
                }
            }
            Row newRow = createNewRow(columnNames, columnFactMap);
            columnFactMap = initialiseMap(columnNames);
            newRows.add(newRow);
        }
        makeRowFields(pivotTable, newRows);
        pivotTable.getRowSection().setRowList(newRows);
    }

    private void makeRowFields(PivotTable pivotTable, List<Row> newRows) {
        List<Row> rowList = pivotTable.getRowSection().getRowList();
        // loop through the new rows
        for (int i = 0; i < newRows.size(); i++) {
            List<Integer> old = rowMap.get(i);
            // get the first old row from the new row
            Row firstRow = rowList.get(old.get(0));
            // get the last old row from the new row
            Row lastRow = rowList.get(old.get(old.size() - 1));
            List<RowField> beginList = firstRow.getRowFieldList();
            List<RowField> endList = lastRow.getRowFieldList();
            for (RowField rowField : beginList) {
                String value = rowField.getValue();
                String endValue = endList.get(beginList.indexOf(rowField)).getValue();
                RowField newRowField = new RowField(value + "-" + endValue);
                newRows.get(i).getRowFieldList().add(newRowField);
            }
        }
    }

    private Row createNewRow(String[] columnNames, Map<String, Integer> columnFactMap) {
        Row row = new Row();
        for (String columnName : columnNames) {
            Fact fact = new Fact(columnFactMap.get(columnName));
            row.getFactList().add(fact);
            fact.setRow(row);
        }
        return row;
    }

    private Map<String, Integer> initialiseMap(String[] columnNames) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String columnName : columnNames) {
            map.put(columnName, 0);
        }
        return map;
    }

    /**
     * 
     * 
     * 
     * @param pivotTable
     * @param rows
     */
    private void generateIndexMap(PivotTable pivotTable, int rows) {
        int originalNrRows = pivotTable.getRowSection().getRowList().size();
        int nrOfRowsPerRow = 0;
        if (rows > 0) {
            nrOfRowsPerRow = (int) Math.ceil(originalNrRows / rows);
        }

        int oldRowIndex = 0;
        for (int i = 0; i < rows; i++) {
            Integer nn = Integer.valueOf(i);
            List<Integer> list = new ArrayList<Integer>();
            for (int o = 0; o < nrOfRowsPerRow; o++) {
                if (oldRowIndex < originalNrRows) {
                    Integer ii = Integer.valueOf(oldRowIndex);
                    list.add(ii);
                    oldRowIndex++;
                }
            }
            rowMap.put(nn, list);
        }
    }

}
