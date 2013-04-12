package org.fao.fi.pivot.manipulation;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.model.CalculatedCollection;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.ColumnSection;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.RowField;
import org.fao.fi.pivot.model.RowHeaderField;
import org.fao.fi.pivot.model.RowSection;
import org.fao.fi.pivot.model.fact.AbstractFact;

public class Transpose {

    /**
     * 
     * This is only tested with 1 dimension in the row section. It will not work correctly with more dimensions in the
     * row section.
     * 
     * 
     * 
     * 
     * @param pivotTable
     * @param indexRowSectionColumn
     *            the column in the row section which should be moved to the ColumnSection, starting from 0.
     */
    public void transpose(PivotTable pivotTable, int indexRowSectionColumn) {
        ColumnSection columnSection = pivotTable.getColumnSection();
        RowSection rowSection = pivotTable.getRowSection();

        // build up new row and column section
        ColumnSection columnSectionNew = new ColumnSection();
        RowSection rowSectionNew = new RowSection();
        List<Row> rowListNew = new ArrayList<Row>();
        rowSectionNew.setRowList(rowListNew);
        List<ColumnField> columnFieldListNew = new ArrayList<ColumnField>();
        columnSectionNew.setColumnFieldList(columnFieldListNew);

        // build up the new rows, columns and facts
        List<Row> rowList = rowSection.getRowList();
        List<ColumnField> columnFieldList = columnSection.getColumnFieldList();
        createRowList(columnFieldList, rowListNew);
        createColumnFieldList(rowList, columnFieldListNew, indexRowSectionColumn);

        // hand over the facts
        handleOverFacts(rowList, columnFieldList, rowListNew, columnFieldListNew);

        // Hand over the concept names.
        columnSectionNew.setConceptName(rowSection.getRowHeaderFieldList().get(indexRowSectionColumn).getConceptName());
        processRowHeaderFields(pivotTable.getRowSection().getRowHeaderFieldList(),
                rowSectionNew.getRowHeaderFieldList(), indexRowSectionColumn, columnSection.getConceptName());

        // hand or the aggregated stuff
        processAggregations(columnSection, rowSection, columnSectionNew, rowSectionNew);

        // renew the pivot table
        pivotTable.setColumnSection(columnSectionNew);
        pivotTable.setRowSection(rowSectionNew);

    }

    private void processAggregations(ColumnSection columnSection, RowSection rowSection,
            ColumnSection columnSectionNew, RowSection rowSectionNew) {
        List<CalculatedCollection> acl = columnSection.getCalculatedCollectionList();
        List<CalculatedCollection> arl = rowSection.getCalculatedCollectionList();
        columnSectionNew.setCalculatedCollectionList(arl);
        rowSectionNew.setCalculatedCollectionList(acl);
    }

    private void processRowHeaderFields(List<RowHeaderField> rowHeaderFieldList,
            List<RowHeaderField> rowHeaderFieldListNew, int indexRowSectionColumn, String conceptName) {
        for (RowHeaderField rowHeaderField : rowHeaderFieldList) {
            rowHeaderFieldListNew.add(rowHeaderField);
        }
        rowHeaderFieldListNew.get(indexRowSectionColumn).setConceptName(conceptName);

    }

    private void createRowList(List<ColumnField> columnFieldList, List<Row> rowListNew) {
        for (ColumnField columnField : columnFieldList) {
            Row newRow = new Row();
            RowField rowFieldNew = new RowField();
            rowFieldNew.setValue(columnField.getValue());
            newRow.getRowFieldList().add(rowFieldNew);
            rowListNew.add(newRow);
        }

    }

    private void createColumnFieldList(List<Row> rowList, List<ColumnField> columnFieldListNew,
            int indexRowSectionColumn) {
        for (Row row : rowList) {
            ColumnField columnFieldNew = new ColumnField();
            columnFieldNew.setValue(row.getRowFieldList().get(indexRowSectionColumn).getValue());
            columnFieldListNew.add(columnFieldNew);
        }
    }

    private void handleOverFacts(List<Row> rowList, List<ColumnField> columnFieldList, List<Row> rowListNew,
            List<ColumnField> columnFieldListNew) {
        int columnIndex = 0;
        // change the references of the facts to the columnField
        for (Row row : rowList) {
            List<AbstractFact> factList = row.getFactList();
            ColumnField columnField = columnFieldListNew.get(columnIndex++);
            for (AbstractFact fact : factList) {
                fact.setColumnField(columnField);
                columnField.getFactList().add(fact);
            }
        }
        int rowIndex = 0;
        // change the references of the facts to the rows
        for (ColumnField columnField : columnFieldList) {
            Row row = rowListNew.get(rowIndex++);
            List<AbstractFact> factList = columnField.getFactList();
            for (AbstractFact fact : factList) {
                fact.setRow(row);
                row.getFactList().add(fact);
            }
        }
    }

}
