package org.fao.fi.tabular2pivot;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.RowField;
import org.fao.fi.pivot.model.RowHeaderField;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.model.fact.StringFact;
import org.fao.fi.tabulardata.model.TabularData;

/**
 * This logic assumes a simple pivottable
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class TabularData2PivotTable {
    public static final int ROWSECTION_INDEX = 2;
    public static final int DATACOLUMN_INDEX = 1;

    public PivotTable convert(TabularData t) {
        String[][] table = t.getTable();
        PivotTable pt = new PivotTable();
        pt.getColumnSection().setConceptName(table[0][1]);
        pt.setFactConceptName(table[0][0]);
        processColumnSection(table, pt);
        processRowSection(table, pt);
        processFacts(table, pt);
        return pt;
    }

    private void processFacts(String[][] table, PivotTable pt) {
        for (int r = ROWSECTION_INDEX; r < table.length; r++) {
            Row row = pt.getRowSection().getRowList().get(r - ROWSECTION_INDEX);
            for (int c = DATACOLUMN_INDEX; c < table[0].length; c++) {
                ColumnField cf = pt.getColumnSection().getColumnFieldList().get(c - DATACOLUMN_INDEX);
                String value = table[r][c];
                AbstractFact fact = null;
                try {
                    fact = new Fact(new Double(value));
                } catch (NumberFormatException e) {
                    fact = new StringFact(value);
                }
                fact.setColumnField(cf);
                fact.setRow(row);
                row.getFactList().add(fact);
                cf.getFactList().add(fact);
            }
        }

    }

    private void processColumnSection(String[][] table, PivotTable pt) {
        for (int c = DATACOLUMN_INDEX; c < table[1].length; c++) {
            ColumnField cf = new ColumnField(table[1][c]);
            pt.getColumnSection().getColumnFieldList().add(cf);
        }
    }

    private void processRowSection(String[][] table, PivotTable pt) {
        RowHeaderField rhf = new RowHeaderField(table[1][0]);
        pt.getRowSection().getRowHeaderFieldList().add(rhf);
        for (int r = ROWSECTION_INDEX; r < table.length; r++) {
            Row row = new Row();
            RowField rowField = new RowField(table[r][0]);
            row.getRowFieldList().add(rowField);
            pt.getRowSection().getRowList().add(row);
        }
    }

}
