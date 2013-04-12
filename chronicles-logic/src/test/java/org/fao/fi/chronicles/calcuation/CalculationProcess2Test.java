package org.fao.fi.chronicles.calcuation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.tabular2pivot.Csv2Pivot;
import org.fao.fi.tabular2pivot.Pivot2Csv;
import org.junit.Test;

public class CalculationProcess2Test {

    PivotTableValidator v = new PivotTableValidator();
    
    @Test
    public void testRun() {
        PivotTable pivotTable = Csv2Pivot       
                .createPivotTableFromCsv("src/test/resources/inputForCalculation2/ChroniclesContainerAestheticProcess.csv");
        assertTrue(v.validate(pivotTable));

        CalculationProcess2 c2 = new CalculationProcess2();
        c2.run(pivotTable,12);
        assertTrue(v.validate(pivotTable));

        List<ColumnField> columns = pivotTable.getColumnSection().getColumnFieldList();
        Pivot2Csv.write2csv(pivotTable, "CalculationProcess2Test.csv");
        assertEquals(12, columns.size());

        for (ColumnField columnField : columns) {
            System.out.println(columnField.getAttributeList().get(0).getValue());
        }
    }

}
