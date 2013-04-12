package org.fao.fi.pivot.manipulation;

import java.util.List;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.Derived;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;

public class RemoveOther {

    private RemoveOther() {
        // Utility classes should not have a public or default constructor
    }

    /**
     * Remove all the rows which are derived from other rows.
     * 
     * 
     * @param pivotTable
     */
    public static void execute(PivotTable pivotTable) {
        List<Row> rows = pivotTable.getRowSection().getRowList();
        List<ColumnField> cs = pivotTable.getColumnSection().getColumnFieldList();
        for (Row row : rows) {
            if (row instanceof Derived) {
                for (ColumnField columnField : cs) {
                    columnField.getFactList().remove(rows.indexOf(row));
                }
                rows.remove(row);
            }
        }
    }

}
