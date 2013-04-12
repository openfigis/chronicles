package org.fao.fi.pivot.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.fao.fi.pivot.calculation.CalculationOnPivotTest;
import org.junit.Test;

public class PivotTableValidatorTest {

    @Test
    public void testValidate() {
        PivotTable pivotTable = CalculationOnPivotTest.createPivotTableMock();
        PivotTableValidator v = new PivotTableValidator();
        assertTrue(v.validate(pivotTable));
    }

    @Test
    public void testGetErrorList() {
        PivotTable pivotTable = CalculationOnPivotTest.createPivotTableMock();
        PivotTableValidator v = new PivotTableValidator();
        v.validate(pivotTable);
        assertEquals(0, v.getErrorList().size());
    }

}
