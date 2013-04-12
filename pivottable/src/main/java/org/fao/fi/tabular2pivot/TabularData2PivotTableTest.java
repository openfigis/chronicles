package org.fao.fi.tabular2pivot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.fao.fi.tabulardata.model.TabularData;
import org.junit.Test;

public class TabularData2PivotTableTest {

    @Test
    public void testConvert1() {
        PivotTable pt = PivotTableMocker.mockIt(2, 3, 1);

        Pivot2Sysout.pivot2SysOut(pt);

        Pivot2Tabular p2t = new Pivot2Tabular();
        p2t.pivot2Tabular(pt);
        TabularData td = new TabularData();
        td.setTable(p2t.pivot2Tabular(pt));

        TabularData2PivotTable td2pt = new TabularData2PivotTable();
        PivotTable ptNew = td2pt.convert(td);

        Pivot2Sysout.pivot2SysOut(pt);

        List<ColumnField> columns = pt.getColumnSection().getColumnFieldList();

        List<ColumnField> columnsNew = ptNew.getColumnSection().getColumnFieldList();
        List<Row> rows = pt.getRowSection().getRowList();
        List<Row> rowsNew = ptNew.getRowSection().getRowList();

        // checking field sizes
        assertEquals(columns.size(), columnsNew.size());
        assertEquals(rows.size(), rowsNew.size());

        // checking values
        for (int i = 0; i < columns.size(); i++) {
            ColumnField cf = columns.get(i);
            ColumnField cfNew = columnsNew.get(i);
            List<AbstractFact> fl = cf.getFactList();
            List<AbstractFact> flNew = cfNew.getFactList();
            for (int j = 0; j < fl.size(); j++) {
                assertEquals(((Fact) fl.get(j)).getValue(), ((Fact) flNew.get(j)).getValue(), 0);
            }
        }

    }

    @Test
    public void testConvert2() {
        PivotTable pt = PivotTableMocker.mockIt(2, 3, 1);

        Pivot2Sysout.pivot2SysOut(pt);

        Pivot2Tabular p2t = new Pivot2Tabular();
        p2t.pivot2Tabular(pt);
        TabularData td = new TabularData();
        td.setTable(p2t.pivot2Tabular(pt));

        TabularData2PivotTable td2pt = new TabularData2PivotTable();
        PivotTable ptNew = td2pt.convert(td);
        PivotTableValidator v = new PivotTableValidator();
        assertTrue(v.validate(ptNew));
        assertEquals(pt.getRowSection().getRowHeaderFieldList().get(0).getConceptName(), ptNew.getRowSection()
                .getRowHeaderFieldList().get(0).getConceptName());
    }

}
