package org.fao.fi.pivot.calculation;

import java.util.Collections;
import java.util.List;

import org.fao.fi.pivot.model.CalculatedCollection;
import org.fao.fi.pivot.model.CalculatedField;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.AbstractFact;

public class SortOnPivot {

    public void sortHighToLowOnAverage(PivotTable pivotTable) {
        CalculatedCollection aggregatedColumnFound = findAverageColumn(pivotTable);
        if (aggregatedColumnFound == null) {
            CalculationOnPivot calculationOnPivot = new CalculationOnPivot();
            calculationOnPivot.doHorizontalCalculationOn(pivotTable, CalculationType.AVG);
            aggregatedColumnFound = findAverageColumn(pivotTable);
        }

        Object[] array = aggregatedColumnFound.getCalculatedFieldList().toArray();
        sortCalculatedCollection(aggregatedColumnFound);
        correctPivotTable(pivotTable, aggregatedColumnFound, array);

    }

    private void correctPivotTable(PivotTable pivotTable, CalculatedCollection aggregatedColumnFound, Object[] array) {

        // sort the aggregated columns
        List<CalculatedField> newList = aggregatedColumnFound.getCalculatedFieldList();

        List<CalculatedCollection> calcList = pivotTable.getColumnSection().getCalculatedCollectionList();
        for (CalculatedCollection calculatedCollection : calcList) {
            if (aggregatedColumnFound != calculatedCollection) {
                CalculatedCollection c = new CalculatedCollection(calculatedCollection.getCalculationType());
                List<CalculatedField> cfl = c.getCalculatedFieldList();
                NewListComperator comp = new NewListComperator(array, newList, cfl);
                Collections.sort(cfl, comp);
            }
        }

        // sort the rows
        List<Row> rowList = pivotTable.getRowSection().getRowList();
        NewListComperator comp = new NewListComperator(array, newList, rowList);
        Collections.sort(rowList, comp);

        // sort the facts
        List<ColumnField> colList = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : colList) {
            List<AbstractFact> fl = columnField.getFactList();
            comp = new NewListComperator(array, newList, fl);
            Collections.sort(fl, comp);
        }

    }

    private CalculatedCollection findAverageColumn(PivotTable pivotTable) {
        List<CalculatedCollection> l = pivotTable.getColumnSection().getCalculatedCollectionList();
        CalculatedCollection aggregatedColumnFound = null;
        for (CalculatedCollection aggregatedColumn : l) {
            if (aggregatedColumn.getCalculationType().equals(CalculationType.AVG)) {
                aggregatedColumnFound = aggregatedColumn;
            }
        }
        return aggregatedColumnFound;
    }

    private void sortCalculatedCollection(CalculatedCollection aggregatedColumnFound) {
        List<CalculatedField> list = aggregatedColumnFound.getCalculatedFieldList();
        Collections.sort(list, new DoubleValueComperator());
    }

}
