package org.fao.fi.chronicles.calcuation;

import org.fao.fi.pivot.calculation.PercentageOnPivot;
import org.fao.fi.pivot.calculation.PhasingOnPivot;
import org.fao.fi.pivot.calculation.SlopeOnPivot;
import org.fao.fi.pivot.logic.ClassificationOnPivot;
import org.fao.fi.pivot.logic.Phase;
import org.fao.fi.pivot.model.PivotTable;
import org.springframework.stereotype.Component;

@Component
public class CalculationProcess3 {

    public static final String[] columnNames = { Phase.U.toString(), Phase.D.toString(), Phase.M.toString(),
            Phase.S.toString(), Phase.R.toString() };
    public static final int ROWS = 11;

    public PivotTable run(PivotTable pivotTable) {

        // create slope table
        PivotTable slopeTable = pivotTablecalulateSlope(pivotTable);

        // create phase table
        createPhaseTable(slopeTable);

        // calculate phases and periods
        calculatePhasesAndPeriods(slopeTable);

        // calculated percentages
        calculatedPercentages(slopeTable);

        return slopeTable;

    }

    private void calculatePhasesAndPeriods(PivotTable pivotTable) {
        PhasingOnPivot.calculate(pivotTable, ROWS, columnNames);
    }

    private void createPhaseTable(PivotTable pivotTabel) {
        ClassificationOnPivot.classify(pivotTabel);
    }

    private PivotTable pivotTablecalulateSlope(PivotTable pivotTable) {
        return SlopeOnPivot.generateSlopePivot(pivotTable);
    }

    private void calculatedPercentages(PivotTable pivotTable) {
        PercentageOnPivot.verticalPercentage(pivotTable);
    }
}
