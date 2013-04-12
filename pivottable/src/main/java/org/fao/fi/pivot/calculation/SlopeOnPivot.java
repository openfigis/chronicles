package org.fao.fi.pivot.calculation;

import java.util.List;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;

/**
 * 
 * Slope calculates the difference between 2 values.
 * 
 * 
 * @author Erik van Ingen
 * 
 */

public class SlopeOnPivot {

    private static String SLOPE = "SLOPE";

    private SlopeOnPivot() {
        // Utility classes should not have a public or default constructor
    }

    /**
     * This logic will return a new table with the slope values. The new table has some references to the original
     * pivotTable, which can be considered as an acceptable bug.
     * 
     * 
     * @param pivotTable
     * @return slopePivotTable
     */
    public static PivotTable generateSlopePivot(PivotTable pivotTable) {
        PivotTable slopePivotTable = new PivotTable();
        copyMetadata(pivotTable, slopePivotTable);
        calculateSlope(pivotTable, slopePivotTable);
        createdRows(pivotTable, slopePivotTable);
        return slopePivotTable;
    }

    private static void createdRows(PivotTable pivotTable, PivotTable slopeTable) {
        for (int i = 1; i < pivotTable.getRowSection().getRowList().size(); i++) {
            int newIndex = i - 1;
            Row row = new Row();
            row.setRowFieldList(pivotTable.getRowSection().getRowList().get(i).getRowFieldList());
            List<ColumnField> cfl = slopeTable.getColumnSection().getColumnFieldList();
            for (ColumnField columnField : cfl) {
                AbstractFact fact = columnField.getFactList().get(newIndex);
                fact.setRow(row);
                row.getFactList().add(fact);
            }
            slopeTable.getRowSection().getRowList().add(row);
        }
    }

    private static void calculateSlope(PivotTable pivotTable, PivotTable slopeTable) {
        List<ColumnField> cl = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : cl) {
            List<AbstractFact> fl = columnField.getFactList();
            double previousValue = 0;
            ColumnField slopeColumn = new ColumnField();
            slopeColumn.setAttributeList(columnField.getAttributeList());
            slopeColumn.setValue(columnField.getValue());
            for (AbstractFact fact : fl) {
                Fact factFound = (Fact) fact;
                if (fl.indexOf(fact) > 0) {
                    Fact slope = new Fact();
                    fact.setColumnField(slopeColumn);
                    slope.setValue(factFound.getValue() - previousValue);
                    slope.setColumnField(slopeColumn);
                    slopeColumn.getFactList().add(slope);
                }
                previousValue = factFound.getValue();
            }
            slopeTable.getColumnSection().getColumnFieldList().add(slopeColumn);
        }
    }

    private static void copyMetadata(PivotTable pivotTable, PivotTable slopeTable) {
        slopeTable.setFactConceptName(SLOPE);
        slopeTable.getColumnSection().setConceptName(pivotTable.getColumnSection().getConceptName());
        slopeTable.getRowSection().setRowHeaderFieldList(pivotTable.getRowSection().getRowHeaderFieldList());
    }

}
