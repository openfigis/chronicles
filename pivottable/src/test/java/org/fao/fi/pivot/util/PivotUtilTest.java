package org.fao.fi.pivot.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.model.fact.StringFact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Test;

public class PivotUtilTest {

    @Test
    public void testCopyFactsFromColumn2Row() {
        int n = 2;
        int startValue = 1;
        PivotTable p = PivotTableMocker.mockIt(n, ++n, n);
        PivotTableValidator v = new PivotTableValidator();
        assertTrue(v.validate(p));
        List<ColumnField> list = p.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : list) {
            List<AbstractFact> afl = columnField.getFactList();
            List<AbstractFact> newList = new ArrayList<AbstractFact>();
            for (AbstractFact abstractFact : afl) {
                StringFact sf = new StringFact("value" + startValue++);
                sf.setColumnField(columnField);
                sf.setRow(p.getRowSection().getRowList().get(afl.indexOf(abstractFact)));
                newList.add(sf);
            }
            columnField.setFactList(newList);
        }
        assertFalse(v.validate(p));
        PivotUtil.copyFactsFromColumn2Row(p);
        assertTrue(v.validate(p));
    }

    @Test
    public void testCopyFactsFromRow2Column1() {
        int n = 2;
        PivotTable p = PivotTableMocker.mockIt(n, ++n, n);
        PivotTableValidator v = new PivotTableValidator();
        PivotUtil.copyFactsFromRow2Column(p);
        assertTrue(v.validate(p));
    }

    @Test
    public void testCopyFactsFromRow2Column2() {
        int n = 2;
        PivotTable p = PivotTableMocker.mockIt(n, ++n, n);
        
        p.getColumnSection().getColumnFieldList().get(0).getFactList().add(new Fact(3.2));
        
        PivotTableValidator v = new PivotTableValidator();
        PivotUtil.copyFactsFromRow2Column(p);
        assertTrue(v.validate(p));
    }
    
    
    
    @Test
    public void testCopyFactsFromRow2EmptyColumn() {
        int n = 2;
        PivotTable p = PivotTableMocker.mockIt(n, ++n, n);
        PivotTableValidator v = new PivotTableValidator();
        Pivot2Sysout.pivot2SysOut(p);
        p.getColumnSection().setColumnFieldList(null);
        PivotUtil.copyFactsFromRow2EmptyColumn(p);
        Pivot2Sysout.pivot2SysOut(p);
        assertTrue(v.validate(p));
    }

}
