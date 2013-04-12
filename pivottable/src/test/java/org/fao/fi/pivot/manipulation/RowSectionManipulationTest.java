package org.fao.fi.pivot.manipulation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.RowHeaderField;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.fao.fi.tabular2pivot.Pivot2Tabular;
import org.junit.Test;

public class RowSectionManipulationTest {

    @Test
    public void testConcatenateColumns() {
        RowSectionManipulation m = new RowSectionManipulation();
        int numberOfRowColumns = 3;
        PivotTable pt = PivotTableMocker.mockIt(2, 5, numberOfRowColumns);
        List<RowHeaderField> rowHeaderFieldList = new ArrayList<RowHeaderField>();
        RowHeaderField r1 = new RowHeaderField(PivotTableMocker.DIMENSION_NAME + "2");
        RowHeaderField r2 = new RowHeaderField(PivotTableMocker.DIMENSION_NAME + "3");
        rowHeaderFieldList.add(r1);
        rowHeaderFieldList.add(r2);
        String newName = "Erik";
        m.concatenateColumns(pt, rowHeaderFieldList, newName);
        assertEquals(newName, pt.getRowSection().getRowHeaderFieldList().get(1).getConceptName());
        assertEquals(numberOfRowColumns - 1, pt.getRowSection().getRowHeaderFieldList().size());
        int numberofColumnsInRowSection = numberOfRowColumns - 1;
        assertEquals(numberofColumnsInRowSection, pt.getRowSection().getRowHeaderFieldList().size());
        List<Row> list = pt.getRowSection().getRowList();
        for (Row row : list) {
            assertEquals(numberofColumnsInRowSection, row.getRowFieldList().size());
        }
        pt.getColumnSection().getConceptName();
        Pivot2Tabular pivot2Tabular = new Pivot2Tabular();
        String[][] table = pivot2Tabular.pivot2Tabular(pt);
        for (String[] row : table) {
            for (String field : row) {
                System.out.print(field + '\t');
            }
            System.out.println();
        }
    }
}
