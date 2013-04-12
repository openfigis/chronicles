package org.fao.fi.pivot.util;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.PivotTableException;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.AbstractFact;

public class PivotUtil {

    private PivotUtil() {
        // Utility classes should not have a public or default constructor
    }

    /**
     * When facts are changed from the columns perspective in the object graph and not populated yet from a row
     * perspective, use this utility.
     * 
     * This utility copies all the facts from a column perspective and 'hangs' them under the correct row.
     * 
     * 
     * @param pivotTable
     */
    public static void copyFactsFromColumn2Row(PivotTable pivotTable) {
        List<ColumnField> cl = pivotTable.getColumnSection().getColumnFieldList();
        List<Row> rl = pivotTable.getRowSection().getRowList();
        for (ColumnField columnField : cl) {
            List<AbstractFact> fl = columnField.getFactList();
            for (AbstractFact abstractFact : fl) {
                int columIndex = cl.indexOf(columnField);
                int rowIndex = fl.indexOf(abstractFact);
                List<AbstractFact> rowFactList = rl.get(rowIndex).getFactList();
                rowFactList.remove(columIndex);
                rowFactList.add(columIndex, abstractFact);

                if (rl.get(rowIndex) != abstractFact.getRow()) {
                    throw new PivotTableException("row found from a column perspective is not correct");
                }

            }
        }
    }

    /**
     * When facts are changed from the row perspective in the object graph and not populated yet from a column
     * perspective, use this utility.
     * 
     * This utility copies all the facts from a row perspective and 'hangs' them under the correct column.
     * 
     * 
     * @param pivotTable
     */
    public static void copyFactsFromRow2Column(PivotTable pivotTable) {
        List<Row> cl = pivotTable.getRowSection().getRowList();
        List<ColumnField> rl = pivotTable.getColumnSection().getColumnFieldList();

        // cut the column field fact lists off when they are too long.
        for (ColumnField columnField : rl) {
            if (columnField.getFactList().size() > cl.size()) {
                columnField.setFactList(columnField.getFactList().subList(0, cl.size()));
            }
        }

        // re hang the facts
        for (Row row : cl) {
            List<AbstractFact> rfl = row.getFactList();
            for (AbstractFact abstractFact : rfl) {
                int rIndex = cl.indexOf(row);
                int cIndex = rfl.indexOf(abstractFact);
                List<AbstractFact> rowFactList = rl.get(cIndex).getFactList();
                System.out.println(rIndex + " " + rowFactList.size());
                if (rIndex < rowFactList.size()) {
                    rowFactList.remove(rIndex);
                }
                rowFactList.add(rIndex, abstractFact);
                if (rl.get(cIndex) != abstractFact.getColumnField()) {
                    throw new PivotTableException("column found from a row perspective is not correct");
                }
            }
        }
    }

    /**
     * 
     * 
     * 
     * 
     * @param pivotTable
     */
    public static void copyFactsFromRow2EmptyColumn(PivotTable pivotTable) {
        List<Row> rowList = pivotTable.getRowSection().getRowList();
        List<ColumnField> columnFieldList = new ArrayList<ColumnField>();
        if (rowList.size() > 0) {
            int numberOfColumns = rowList.get(0).getFactList().size();
            for (int i = 0; i < numberOfColumns; i++) {
                ColumnField columnField = new ColumnField();
                columnFieldList.add(columnField);
            }
            for (Row row : rowList) {
                List<AbstractFact> fl = row.getFactList();
                for (AbstractFact abstractFact : fl) {
                    int cIndex = fl.indexOf(abstractFact);
                    ColumnField columnField = columnFieldList.get(cIndex);
                    columnField.getFactList().add(abstractFact);
                    abstractFact.setColumnField(columnFieldList.get(cIndex));
                }
            }
            pivotTable.getColumnSection().setColumnFieldList(columnFieldList);
        }
    }

}
