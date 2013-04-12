package org.fao.fi.pivot.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Test;

public class SlopeOnPivotTest {

    @Test
    public void testGenerateSlopePivot() {
        int rows = 2;
        int columns = 2;
        int rowColumns = 3;
        PivotTable p = PivotTableMocker.mockIt(rows, columns, rowColumns);

        ((Fact) p.getRowSection().getRowList().get(0).getFactList().get(0)).setValue(0.01);
        ((Fact) p.getRowSection().getRowList().get(0).getFactList().get(1)).setValue(20.0);
        ((Fact) p.getRowSection().getRowList().get(1).getFactList().get(0)).setValue(0.08);
        ((Fact) p.getRowSection().getRowList().get(1).getFactList().get(1)).setValue(200.0);
        PivotTable s = SlopeOnPivot.generateSlopePivot(p);
        Pivot2Sysout.pivot2SysOut(s);

        PivotTableValidator v = new PivotTableValidator();

        assertEquals(p.getColumnSection().getColumnFieldList().size(), s.getColumnSection().getColumnFieldList().size());

        assertTrue(v.validate(s));

        assertEquals(p.getRowSection().getRowList().size() - 1, s.getRowSection().getRowList().size());
        assertEquals(0.07, ((Fact) s.getRowSection().getRowList().get(0).getFactList().get(0)).getValue(), 1e-15);
        assertEquals(180, ((Fact) s.getRowSection().getRowList().get(0).getFactList().get(1)).getValue(), 0);

    }

}
