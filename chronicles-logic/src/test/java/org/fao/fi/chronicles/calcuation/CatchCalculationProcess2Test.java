package org.fao.fi.chronicles.calcuation;

import static org.junit.Assert.assertTrue;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.tabular2pivot.Pivot2Csv;
import org.junit.Before;
import org.junit.Test;

public class CatchCalculationProcess2Test {

    PivotTable pivotTable;
    PivotTableValidator v = new PivotTableValidator();;

    @Before
    public void before() {
        FishstatProcessTestHelper f = new FishstatProcessTestHelper();
        pivotTable = f.getPivotTable();
        CalculationProcess1 c = new CalculationProcess1();
        c.run(pivotTable,12);
    }

    @Test
    public void testRun() {
        CatchCalculationProcess2 c = new CatchCalculationProcess2();
        c.run(pivotTable);
        assertTrue(v.validate(pivotTable));
        Pivot2Csv.write2csv(pivotTable, "CatchCalculationProcess2Test.csv");
    }

}
