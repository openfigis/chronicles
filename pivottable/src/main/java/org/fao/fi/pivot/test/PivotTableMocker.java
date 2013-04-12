package org.fao.fi.pivot.test;

import org.fao.fi.pivot.logic.Phase;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.RowField;
import org.fao.fi.pivot.model.RowHeaderField;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.model.fact.StringFact;

public class PivotTableMocker {

    private FactType factType;

    public final static String DIMENSION_NAME = "dName";
    public final static String ROWFIELDNAME = "row";
    public final static String COLUMN_DIMENSION_NAME = "year";
    public final static String FACT_CONCEPT_NAME = "Fact";
    public final static Phase PHASES[] = { Phase.D, Phase.M, Phase.R, Phase.S, Phase.U };

    private int factIndex = 1;
    private int phaseIndex = 0;

    enum FactType {
        numeric, alphanumeric
    }

    // private PivotTableMocker() {
    // // Utility classes should not have a public or default constructor
    // }

    public PivotTableMocker(FactType factType) {
        this.factType = factType;
    }

    public static final PivotTable mockItString(int rows, int columns, int rowColumns) {
        PivotTableMocker m = new PivotTableMocker(FactType.alphanumeric);
        PivotTable pivotTable = m.mock(rows, columns, rowColumns);
        return pivotTable;
    }

    public static final PivotTable mockIt(int rows, int columns, int rowColumns) {
        PivotTableMocker m = new PivotTableMocker(FactType.numeric);
        return m.mock(rows, columns, rowColumns);
    }

    private PivotTable mock(int rows, int columns, int rowColumns) {
        PivotTable pt = new PivotTable();
        pt.setFactConceptName(FACT_CONCEPT_NAME);

        int rowFieldIndex = 1;
        pt.getColumnSection().setConceptName(COLUMN_DIMENSION_NAME);

        for (int i = 1; i <= rowColumns; i++) {
            RowHeaderField rowHeaderField = new RowHeaderField(DIMENSION_NAME + i);
            pt.getRowSection().getRowHeaderFieldList().add(rowHeaderField);
        }

        for (int c = 0; c < columns; c++) {
            ColumnField columnField = new ColumnField();
            int columnValueImitator = 2001 + c;
            columnField.setValue(Integer.toString(columnValueImitator));
            pt.getColumnSection().getColumnFieldList().add(columnField);
        }

        for (int r = 0; r < rows; r++) {
            Row row = new Row();
            for (int c = 0; c < columns; c++) {
                AbstractFact fact = createFact();
                fact.setRow(row);
                ColumnField columnField = pt.getColumnSection().getColumnFieldList().get(c);
                fact.setColumnField(columnField);
                columnField.getFactList().add(fact);
                row.getFactList().add(fact);
            }

            for (int i = 1; i <= rowColumns; i++) {
                RowField rowField = new RowField();
                rowField.setValue(ROWFIELDNAME + rowFieldIndex++);
                row.getRowFieldList().add(rowField);
            }
            pt.getRowSection().getRowList().add(row);
        }
        return pt;
    }

    private AbstractFact createFact() {
        AbstractFact fact = null;
        if (factType.equals(FactType.numeric)) {
            fact = new Fact(factIndex++);
        }

        if (factType.equals(FactType.alphanumeric)) {
            fact = new StringFact(PHASES[phaseIndex++].toString());
            if (phaseIndex == PHASES.length) {
                phaseIndex = 0;
            }
        }
        return fact;
    }

}
