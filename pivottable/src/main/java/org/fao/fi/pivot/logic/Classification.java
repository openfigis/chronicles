package org.fao.fi.pivot.logic;

import java.util.HashSet;
import java.util.Set;

import org.fao.fi.pivot.PivotTableException;

/**
 * This classification represents the rules described in
 * http://km.fao.org/FIGISwiki/index.php/Chronicles#Slope_Phase_rules
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class Classification {

    private Set<Phase> phaseSet = new HashSet<Phase>();
    private Phase previousPhase = null;

    private static Object[][] nextTable = null;
    private static Object[][] firstTable = null;
    static {
        // first time when slope is evaluated
        Object rowA[] = { NumericPhase.up, Phase.D };//
        Object rowB[] = { NumericPhase.equal, Phase.D };
        Object rowC[] = { NumericPhase.down, Phase.S };
        Object[][] newFirstTable = { rowA, rowB, rowC };
        firstTable = newFirstTable;

        // subsequent times when a slope is evaluated
        Object row1[] = { Phase.U, NumericPhase.up, Phase.DR };//
        Object row2[] = { Phase.U, NumericPhase.equal, Phase.U };
        Object row3[] = { Phase.U, NumericPhase.down, Phase.U };
        Object row4[] = { Phase.D, NumericPhase.up, Phase.D };
        Object row5[] = { Phase.D, NumericPhase.equal, Phase.M };
        Object row6[] = { Phase.D, NumericPhase.down, Phase.S };
        Object row7[] = { Phase.M, NumericPhase.up, Phase.DR };//
        Object row8[] = { Phase.M, NumericPhase.equal, Phase.M };
        Object row9[] = { Phase.M, NumericPhase.down, Phase.S };
        Object row10[] = { Phase.S, NumericPhase.up, Phase.R };
        Object row11[] = { Phase.S, NumericPhase.equal, Phase.U };
        Object row12[] = { Phase.S, NumericPhase.down, Phase.S };
        Object row13[] = { Phase.R, NumericPhase.up, Phase.R };
        Object row14[] = { Phase.R, NumericPhase.equal, Phase.M };
        Object row15[] = { Phase.R, NumericPhase.down, Phase.S };
        Object[][] newNextTable = { row1, row2, row3, row4, row5, row6, row7, row8, row9, row10, row11, row12, row13,
                row14, row15 };
        nextTable = newNextTable;

    }

    public Phase determinePhase(NumericPhase numericPhase) {
        Phase phase = null;
        if (previousPhase == null) {
            // first slope
            for (Object[] row : firstTable) {
                NumericPhase p = (NumericPhase) row[0];
                if (numericPhase.equals(p)) {
                    phase = (Phase) row[1];
                }
            }
        } else {
            // subsequent slope
            for (Object[] row : nextTable) {
                Phase p = (Phase) row[0];
                NumericPhase n = (NumericPhase) row[1];
                if (previousPhase.equals(p) && numericPhase.equals(n)) {
                    phase = (Phase) row[2];
                }
            }
            if (phase.equals(Phase.DR)) {
                if (phaseSet.contains(Phase.D)) {
                    // Slope goes up and there was already a development phase found in the past, new one is called
                    // recovering
                    phase = Phase.R;
                } else {
                    // first time that the slope goes up.
                    phase = Phase.D;
                }
            }
        }
        if (phase == null) {
            throw new PivotTableException("No phase found!");
        }
        previousPhase = phase;
        phaseSet.add(phase);
        return phase;
    }

}
