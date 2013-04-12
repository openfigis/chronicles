package org.fao.fi.chronicles.calcuation;

import org.fao.fi.pivot.calculation.CalculationOnPivot;
import org.fao.fi.pivot.model.PivotTable;
import org.springframework.stereotype.Component;

@Component
public class CatchCalculationProcess2 {

    public void run(PivotTable pivotTable) {

        // sum up the totals into one row
        sumUpTotals(pivotTable);

    }

    private void sumUpTotals(PivotTable pivotTable) {
        CalculationOnPivot calculationOnPivot = new CalculationOnPivot();
        calculationOnPivot.summarizeTop(pivotTable);
    }
}
