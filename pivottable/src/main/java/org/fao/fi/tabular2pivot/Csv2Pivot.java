package org.fao.fi.tabular2pivot;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.tabulardata.model.TabularData;

public class Csv2Pivot {

    private Csv2Pivot() {
        // Utility classes should not have a public or default constructor
    }

    /**
     * convert a csv file, representing a pivot table, into a PivotTable object.
     * 
     * 
     * @param csvPivotTableFileName
     * @return
     */
    public static PivotTable createPivotTableFromCsv(String csvPivotTableFileName) {
        Csv2Tabular c = new Csv2Tabular();
        TabularData t = c.parseFile2TabularData(csvPivotTableFileName);
        TabularData2PivotTable t2p = new TabularData2PivotTable();
        PivotTable pivotTable = t2p.convert(t);
        return pivotTable;
    }

}
