package org.fao.fi.chronicles.calcuation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.fao.fi.pivot.model.Attribute;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.tabular2pivot.Csv2Pivot;
import org.fao.fi.tabular2pivot.Pivot2Csv;
import org.junit.Test;

public class CalculationProcess3Test {
    PivotTableValidator v = new PivotTableValidator();

    @Test
    public void testRun() {
        PivotTable pivotTable = Csv2Pivot
                .createPivotTableFromCsv("src/test/resources/inputForCalculation3/CalculationProcess2Test.csv");

        // these numbers are copied from CalculationProcess2Test
        int nrOfResources[] = { 14, 3, 8, 12, 9, 32, 26, 31, 3, 8, 14, 40 };
        List<ColumnField> columns = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : columns) {
            Attribute a = new Attribute(nrOfResources[columns.indexOf(columnField)]);
            columnField.getAttributeList().add(a);
        }

        assertTrue(v.validate(pivotTable));
        CalculationProcess3 c3 = new CalculationProcess3();
        PivotTable s = c3.run(pivotTable);
        Pivot2Csv.write2csv(s, "CalculationProcess3Test.csv");
        assertEquals(CalculationProcess3.ROWS, s.getRowSection().getRowList().size());
        assertEquals(CalculationProcess3.columnNames.length, s.getColumnSection().getColumnFieldList().size());
    }

}
