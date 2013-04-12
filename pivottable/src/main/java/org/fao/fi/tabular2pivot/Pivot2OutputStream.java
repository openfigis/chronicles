package org.fao.fi.tabular2pivot;

import org.fao.fi.pivot.model.PivotTable;

public class Pivot2OutputStream {

    private Pivot2OutputStream() {
        // Utility classes should not have a public or default constructor
    }

    public final static void write2csv(PivotTable pivotTable, String fileName) {
        Pivot2Tabular pivot2Tabular = new Pivot2Tabular();
        String tabularPivot[][] = pivot2Tabular.pivot2Tabular(pivotTable);
        Tabular2Csv.write2csv(tabularPivot, fileName);
    }

}
