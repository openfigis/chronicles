package org.fao.fi.pivot.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.model.fact.StringFact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Test;

public class ClassificationOnPivotTest {

    @Test
    public void testClassify() {
        int rows = 2;
        int columns = 2;
        int rowColumns = 3;
        PivotTable p = PivotTableMocker.mockIt(rows, columns, rowColumns);

        ((Fact) p.getRowSection().getRowList().get(0).getFactList().get(0)).setValue(0.01);
        ((Fact) p.getRowSection().getRowList().get(0).getFactList().get(1)).setValue(20.0);
        ((Fact) p.getRowSection().getRowList().get(1).getFactList().get(0)).setValue(0.08);
        ((Fact) p.getRowSection().getRowList().get(1).getFactList().get(1)).setValue(200.0);

        int originalColumnsSize = p.getColumnSection().getColumnFieldList().size();
        int originalRowListSize = p.getRowSection().getRowList().size();

        Pivot2Sysout.pivot2SysOut(p);

        ClassificationOnPivot.classify(p);
        Pivot2Sysout.pivot2SysOut(p);
        PivotTableValidator v = new PivotTableValidator();
        assertTrue(v.validate(p));

        assertEquals("PHASE", p.getFactConceptName());
        assertEquals(originalColumnsSize, p.getColumnSection().getColumnFieldList().size());
        assertEquals(originalRowListSize, p.getRowSection().getRowList().size());
        assertEquals("D", ((StringFact) p.getRowSection().getRowList().get(0).getFactList().get(0)).getValue());
        assertEquals("D", ((StringFact) p.getRowSection().getRowList().get(0).getFactList().get(1)).getValue());

    }

}
