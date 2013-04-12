package org.fao.fi.chronicles.aesthetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.fao.fi.chronicles.calcuation.FishstatProcessTestHelper;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.tabular2pivot.Pivot2Csv;
import org.fao.fi.tabular2pivot.Pivot2Tabular;
import org.junit.Before;
import org.junit.Test;

public class AestheticProcessTest {

    PivotTable pivotTable;


    @Before
    public void before() {
        FishstatProcessTestHelper f = new FishstatProcessTestHelper();
        pivotTable = f.getPivotTable();
    }
    
    
    
    @Test
    public void testRun() {

        int numberOfColumns = pivotTable.getColumnSection().getColumnFieldList().size();
        int numberOfRows = pivotTable.getRowSection().getRowList().size() ;

        AestheticProcess aestheticProcess = new AestheticProcess();
        aestheticProcess.run(pivotTable);

        PivotTableValidator v = new PivotTableValidator();
        assertTrue(v.validate(pivotTable));

        // check whether the column tax and area are concatenated.
        assertEquals(1, pivotTable.getRowSection().getRowHeaderFieldList().size());
        assertEquals(AestheticProcess.RESOURCE, pivotTable.getColumnSection().getConceptName());

        List<Row> rowList = pivotTable.getRowSection().getRowList();
        for (Row row : rowList) {
            assertEquals(pivotTable.getRowSection().getRowHeaderFieldList().size(), row.getRowFieldList().size());
        }

        // check whether the transposal did take place
        assertEquals(numberOfColumns, pivotTable.getRowSection().getRowList().size());
        assertEquals(numberOfRows, pivotTable.getColumnSection().getColumnFieldList().size());

        // check whether the other table has been removed.
        Pivot2Tabular pivot2Tabular = new Pivot2Tabular();
        String tabularPivot[][] = pivot2Tabular.pivot2Tabular(pivotTable);
        for (String[] row : tabularPivot) {
            for (String field : row) {
                if (field != null) {
                    assertFalse(field.contains("OTHER"));
                }
            }
        }

        Pivot2Csv.write2csv(pivotTable, "AestheticProcessTest.csv");
    }

}
