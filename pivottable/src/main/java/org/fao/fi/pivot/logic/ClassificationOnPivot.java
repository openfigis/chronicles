package org.fao.fi.pivot.logic;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.model.fact.StringFact;
import org.fao.fi.pivot.util.PivotUtil;

/**
 * Classify values in the columns in the classifications (U,D,M,S).
 * 
 * U = under developed D = developed M = mature s = senescent
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class ClassificationOnPivot {

    private static final double SIG_NEG = -0.05;
    private static final double SIG_POS = 0.05;
    private static final String FACT_CONCEPT_NAME = "PHASE";

    private ClassificationOnPivot() {
        // Utility classes should not have a public or default constructor
    }

    public static void classify(PivotTable pivotTable) {

        // classify the rest.
        List<ColumnField> list = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : list) {
            List<AbstractFact> facts = columnField.getFactList();
            List<AbstractFact> stringsfacts = new ArrayList<AbstractFact>();
            Classification classification = new Classification();
            for (AbstractFact fact : facts) {
                Fact realFact = (Fact) fact;
                StringFact stringFact = classify(classification, realFact);
                stringsfacts.add(stringFact);
                stringFact.setColumnField(fact.getColumnField());
                stringFact.setRow(fact.getRow());
            }
            columnField.setFactList(stringsfacts);
        }
        PivotUtil.copyFactsFromColumn2Row(pivotTable);
        pivotTable.setFactConceptName(FACT_CONCEPT_NAME);
    }

    /**
     * 
     * @param classification
     * @param fact
     * @return
     */
    private static StringFact classify(Classification classification, Fact fact) {
        double value = fact.getValue();
        NumericPhase numericPhase = null;
        if (value < SIG_NEG) {
            numericPhase = NumericPhase.down;
        }
        if (value >= SIG_NEG && value <= SIG_POS) {
            numericPhase = NumericPhase.equal;
        }
        if (value > SIG_POS) {
            numericPhase = NumericPhase.up;
        }
        Phase phase = classification.determinePhase(numericPhase);
        StringFact stringFact = new StringFact(phase.name());
        stringFact.setColumnField(fact.getColumnField());
        stringFact.setRow(fact.getRow());
        return stringFact;
    }

}
