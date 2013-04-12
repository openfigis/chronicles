package org.fao.fi.pivot.manipulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.fao.fi.pivot.model.Derived;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.RowField;
import org.fao.fi.pivot.model.RowHeaderField;

public class RowSectionManipulation {

    public void concatenateColumns(PivotTable pivotTable, List<RowHeaderField> rowHeaderFieldList, String newName) {
        List<RowHeaderField> existingrowHeaderFieldList = pivotTable.getRowSection().getRowHeaderFieldList();
        Collection<RowHeaderField> rowHeaderFieldListToBeRemoved = new LinkedList<RowHeaderField>();

        List<Integer> indexList = new ArrayList<Integer>();

        for (RowHeaderField rowHeaderField : rowHeaderFieldList) {
            int i = 0;
            for (RowHeaderField rowHeaderFieldExisting : existingrowHeaderFieldList) {
                if (rowHeaderField.getConceptName().equals(rowHeaderFieldExisting.getConceptName())) {
                    indexList.add(i);
                    if (indexList.size() > 1) {
                        // the first one should be kept, the rest can go
                        rowHeaderFieldListToBeRemoved.add(rowHeaderFieldExisting);
                    }
                }
                i++;
            }
        }

        // process the row header fields
        pivotTable.getRowSection().getRowHeaderFieldList().get(indexList.get(0)).setConceptName(newName);
        pivotTable.getRowSection().getRowHeaderFieldList().removeAll(rowHeaderFieldListToBeRemoved);

        List<Row> rowList = pivotTable.getRowSection().getRowList();

        // process the row fields
        for (Row row : rowList) {
            List<RowField> rowFieldList = row.getRowFieldList();
            String rowFieldValue = "";
            Collection<RowField> toBeRemoved = new LinkedList<RowField>();
            for (Integer index : indexList) {
                RowField found = rowFieldList.get(index);
                if (row instanceof Derived) {
                    rowFieldValue = found.getValue();
                } else {
                    rowFieldValue = rowFieldValue + found.getValue();
                }

                if (rowFieldValue.length() > 0) {
                    // all elements except the first should be romoved
                    toBeRemoved.add(found);
                }
            }
            RowField rowField = new RowField(rowFieldValue);
            rowFieldList.set(indexList.get(0), rowField);
            rowFieldList.removeAll(toBeRemoved);
        }

    }
}
