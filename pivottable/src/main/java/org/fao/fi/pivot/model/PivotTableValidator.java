package org.fao.fi.pivot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.model.fact.AbstractFact;

/**
 * 
 * @author Erik van Ingen
 * 
 */

public class PivotTableValidator implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7675628769654846643L;
    private List<String> errorList;

    public boolean validate(PivotTable pivotTable) {
        errorList = new ArrayList<String>();
        String errorMessage;
        List<ColumnField> columnFieldList = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : columnFieldList) {
            if (pivotTable.getRowSection().getRowList().size() != columnField.getFactList().size()) {
                errorMessage = "pivotTable.getRowSection().getRowList().size() != columnField.getFactList().size() "
                        + pivotTable.getRowSection().getRowList().size() + "!=" + columnField.getFactList().size();
                errorList.add(errorMessage);
            }
        }

        List<Row> rowList = pivotTable.getRowSection().getRowList();
        for (Row row : rowList) {
            if (pivotTable.getColumnSection().getColumnFieldList().size() != row.getFactList().size()) {
                errorMessage = "pivotTable.getColumnSection().getColumnFieldList().size() == row.getFactList().size() "
                        + pivotTable.getColumnSection().getColumnFieldList().size() + "!=" + row.getFactList().size();
                errorList.add(errorMessage);
            }
        }
        checkRowSectionConsistency(pivotTable);
        checkConsistenceOfAbstractFactReferences(pivotTable);
        boolean valid = true;
        if (errorList.size() > 0) {
            valid = false;
        }
        return valid;
    }

    private void checkConsistenceOfAbstractFactReferences(PivotTable pivotTable) {
        List<ColumnField> columnFieldList = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : columnFieldList) {
            List<AbstractFact> factList = columnField.getFactList();
            for (AbstractFact fact : factList) {
                AbstractFact found = pivotTable.getRowSection().getRowList().get(factList.indexOf(fact)).getFactList()
                        .get(columnFieldList.indexOf(columnField));
                if (fact != found) {
                    String errorMessage = "Fact referred by the column is not the same fact as referred to as from the row.";
                    errorList.add(errorMessage);
                }

                if (fact.getColumnField() != columnField) {
                    String errorMessage = "AbstractFact has wrong reference to ColumnField";
                    errorList.add(errorMessage);
                }
            }
        }
        List<Row> rows = pivotTable.getRowSection().getRowList();
        for (Row row : rows) {
            List<AbstractFact> factList = row.getFactList();
            for (AbstractFact fact : factList) {
                if (fact.getRow() != row) {
                    String errorMessage = "AbstractFact has wrong reference to Row";
                    errorList.add(errorMessage);
                }
            }
        }
    }

    /**
     * The number of header fields in the row section should be the same as the number of the values provided for it.
     * 
     * @param pivotTable
     */
    private void checkRowSectionConsistency(PivotTable pivotTable) {
        int rowSectionColumnHeaders = pivotTable.getRowSection().getRowHeaderFieldList().size();
        List<Row> rowList = pivotTable.getRowSection().getRowList();
        int i = 0;
        int sample = 0;
        for (Row row : rowList) {
            sample = row.getRowFieldList().size();
            if (row.getRowFieldList().size() != rowSectionColumnHeaders) {
                i++;
            }
        }
        if (i > 0) {
            String errorMessage = "pivotTable.getRowSection().getRowHeaderFieldList().size() != row.getRowFieldList().size() : "
                    + rowSectionColumnHeaders + " != " + sample;
            errorList.add(errorMessage);
        }
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public String getErrorString() {
        String cumulated = "";
        for (String row : errorList) {
            cumulated = cumulated + row + "\n";
        }
        return cumulated;
    }

}
