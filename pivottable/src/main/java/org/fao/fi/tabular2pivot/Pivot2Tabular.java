package org.fao.fi.tabular2pivot;

import java.util.List;

import org.fao.fi.pivot.model.CalculatedCollection;
import org.fao.fi.pivot.model.CalculatedField;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.RowField;
import org.fao.fi.pivot.model.RowHeaderField;
import org.fao.fi.pivot.model.fact.AbstractFact;

public class Pivot2Tabular {

    private static final int COLUMNSECTION_NUMBER_OF_ROWS = 2;

    /**
     * This converts a PivotTable into a tabular representation of a PivotTable.
     * 
     * @param pivotTable
     * @return
     */
    public String[][] pivot2Tabular(PivotTable pivotTable) {

        int numberOfColumnsInRowSection = pivotTable.getRowSection().getRowHeaderFieldList().size();

        int numberOfColumnsInTabular = numberOfColumnsInRowSection
                + pivotTable.getColumnSection().getColumnFieldList().size()
                + pivotTable.getColumnSection().getCalculatedCollectionList().size();
        int numberOfCalculatedCollections = pivotTable.getRowSection().getCalculatedCollectionList().size();
        int numberOfRows = pivotTable.getRowSection().getRowList().size() + COLUMNSECTION_NUMBER_OF_ROWS
                + numberOfCalculatedCollections;
        String tabularPresentationOfPivot[][] = new String[numberOfRows][numberOfColumnsInTabular];

        addColumnSection2Tabular(pivotTable, tabularPresentationOfPivot);
        addRowSection2Tabular(pivotTable, tabularPresentationOfPivot);
        addRows2Tabular(pivotTable, tabularPresentationOfPivot);
        addAggregatedRows2Tabular(pivotTable, tabularPresentationOfPivot);
        addAggregatedColumns2Tabular(pivotTable, tabularPresentationOfPivot);
        return tabularPresentationOfPivot;
    }

    private void addColumnSection2Tabular(PivotTable pivotTable, String[][] tabularPresentationOfPivot) {
        tabularPresentationOfPivot[0][0] = pivotTable.getFactConceptName();
        tabularPresentationOfPivot[0][1] = pivotTable.getColumnSection().getConceptName();
        List<RowHeaderField> rowHeaderFieldList = pivotTable.getRowSection().getRowHeaderFieldList();
        List<ColumnField> cfl = pivotTable.getColumnSection().getColumnFieldList();
        for (int i = 0; i < cfl.size(); i++) {
            int targetIndex = i + rowHeaderFieldList.size();
            tabularPresentationOfPivot[1][targetIndex] = cfl.get(i).getValue();
        }
    }

    private void addRowSection2Tabular(PivotTable pivotTable, String[][] tabularPresentationOfPivot) {
        List<RowHeaderField> rowHeaderFieldList = pivotTable.getRowSection().getRowHeaderFieldList();
        for (int i = 0; i < rowHeaderFieldList.size(); i++) {
            tabularPresentationOfPivot[1][i] = rowHeaderFieldList.get(i).getConceptName();
        }

    }

    private void addRows2Tabular(PivotTable pivotTable, String[][] tabularPresentationOfPivot) {
        List<Row> rowList = pivotTable.getRowSection().getRowList();
        int tabularRowIndex = 2;
        int tabularColumnIndex = 0;
        for (Row row : rowList) {
            List<RowField> rowFieldList = row.getRowFieldList();
            for (RowField rowField : rowFieldList) {
                tabularPresentationOfPivot[tabularRowIndex][tabularColumnIndex++] = rowField.getValue();
            }
            List<AbstractFact> factList = row.getFactList();

            for (AbstractFact fact : factList) {
                String value = null;
                if (fact != null) {
                    value = fact.toString();
                }
                tabularPresentationOfPivot[tabularRowIndex][tabularColumnIndex++] = value;
            }
            tabularRowIndex++;
            tabularColumnIndex = 0;
        }

    }

    private void addAggregatedRows2Tabular(PivotTable pivotTable, String[][] tabularPresentationOfPivot) {
        int indexLastRow = tabularPresentationOfPivot.length - 1;
        int numberOfCalculatedCollections = pivotTable.getRowSection().getCalculatedCollectionList().size();
        int indexOfFirstCalculatedCollection = indexLastRow - numberOfCalculatedCollections + 1;
        int indexOfFirstValueFieldInCalculatedCollection = pivotTable.getRowSection().getRowHeaderFieldList().size();
        List<CalculatedCollection> aggregatedRows = pivotTable.getRowSection().getCalculatedCollectionList();
        for (CalculatedCollection aggregatedRow : aggregatedRows) {
            String newCalculatedCollection[] = new String[tabularPresentationOfPivot[0].length];
            List<CalculatedField> fields = aggregatedRow.getCalculatedFieldList();
            newCalculatedCollection[0] = aggregatedRow.getCalculationType().toString();
            int i = indexOfFirstValueFieldInCalculatedCollection;
            for (CalculatedField aggregatedRowField : fields) {
                newCalculatedCollection[i++] = new Double(aggregatedRowField.getValue()).toString();
            }
            tabularPresentationOfPivot[indexOfFirstCalculatedCollection] = newCalculatedCollection;
            indexOfFirstCalculatedCollection++;
        }
    }

    private void addAggregatedColumns2Tabular(PivotTable pivotTable, String[][] tabularPresentationOfPivot) {
        int xPositionIndex = pivotTable.getRowSection().getRowHeaderFieldList().size()
                + pivotTable.getColumnSection().getColumnFieldList().size();
        int yPostionIndex = COLUMNSECTION_NUMBER_OF_ROWS;
        int yIndexOfConceptName = COLUMNSECTION_NUMBER_OF_ROWS - 1;
        List<CalculatedCollection> list = pivotTable.getColumnSection().getCalculatedCollectionList();
        int x = xPositionIndex;
        for (CalculatedCollection aggregatedColumn : list) {
            tabularPresentationOfPivot[yIndexOfConceptName][x] = aggregatedColumn.getCalculationType().toString();
            int y = yPostionIndex;
            List<CalculatedField> fields = aggregatedColumn.getCalculatedFieldList();
            for (CalculatedField calculatedField : fields) {
                tabularPresentationOfPivot[y++][x] = new Double(calculatedField.getValue()).toString();
            }
            x++;
        }
    }

}
