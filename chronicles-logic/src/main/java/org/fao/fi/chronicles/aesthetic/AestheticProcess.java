package org.fao.fi.chronicles.aesthetic;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.chronicles.fishstatj.FishstatJConcepts;
import org.fao.fi.pivot.manipulation.RemoveOther;
import org.fao.fi.pivot.manipulation.RowSectionManipulation;
import org.fao.fi.pivot.manipulation.Transpose;
import org.fao.fi.pivot.model.CalculatedCollection;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.RowHeaderField;

public class AestheticProcess {

    public final static String RESOURCE = "RESOURCE";

    public void run(PivotTable pivotTable) {
        // concatenate the columns area with taxonomic code
        concateColumns(pivotTable);

        // remove OTHER stuff.
        removeOther(pivotTable);

        // resources on the top row
        tranposeTable(pivotTable);

        // remove all the aggregated columns and rows.
        removeAggregatedStuff(pivotTable);

    }

    private void tranposeTable(PivotTable pivotTable) {
        Transpose t = new Transpose();
        t.transpose(pivotTable, 0);
    }

    private void concateColumns(PivotTable pivotTable) {
        RowSectionManipulation m = new RowSectionManipulation();
        List<RowHeaderField> rowHeaderFieldList = new ArrayList<RowHeaderField>();
        List<RowHeaderField> currentRowHeaderFieldList = pivotTable.getRowSection().getRowHeaderFieldList();
        for (RowHeaderField rowHeaderField : currentRowHeaderFieldList) {
            if (rowHeaderField.getConceptName().equals(FishstatJConcepts.ALPHA_3_CODE)
                    || rowHeaderField.getConceptName().equals(FishstatJConcepts.AREA)) {
                rowHeaderFieldList.add(rowHeaderField);
            }

        }
        m.concatenateColumns(pivotTable, rowHeaderFieldList, RESOURCE);
    }

    private void removeAggregatedStuff(PivotTable pivotTable) {
        pivotTable.getRowSection().setCalculatedCollectionList(new ArrayList<CalculatedCollection>());
        pivotTable.getColumnSection().setCalculatedCollectionList(new ArrayList<CalculatedCollection>());
    }

    private void removeOther(PivotTable pivotTable) {
        RemoveOther.execute(pivotTable);
    }

}
