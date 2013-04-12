package org.fao.fi.pivot.test;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.tabular2pivot.Pivot2Tabular;

public class Pivot2Sysout {

    private Pivot2Sysout() {
        // Utility classes should not have a public or default constructor
    }

    /**
     * print to sys out
     * 
     * @param pivotTable
     */
    public static void pivot2SysOut(PivotTable pivotTable) {
        Pivot2Tabular pivot2Tabular = new Pivot2Tabular();
        String tabularPivot[][] = pivot2Tabular.pivot2Tabular(pivotTable);
        for (String[] row : tabularPivot) {
            for (String field : row) {
                System.out.print(field + '\t');
            }
            System.out.println();
        }
    }

}
