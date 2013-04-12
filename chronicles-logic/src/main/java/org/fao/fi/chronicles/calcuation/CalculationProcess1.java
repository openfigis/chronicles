package org.fao.fi.chronicles.calcuation;

import org.fao.fi.pivot.calculation.CalculationOnPivot;
import org.fao.fi.pivot.calculation.CalculationType;
import org.fao.fi.pivot.calculation.SortOnPivot;
import org.fao.fi.pivot.model.PivotTable;
import org.springframework.stereotype.Component;

@Component
public class CalculationProcess1 {

    public void run(PivotTable pivotTable, int top) {

        // add columns with average.
        addColumnWithAverage(pivotTable);

        // sort high to low on average
        sortHighToLowOnAverage(pivotTable);

        // calculate Totals Per Year
        calculateTotalsPerYear(pivotTable);

        // select top 200
        selectTopX(pivotTable, top);

    }

    private void addColumnWithAverage(PivotTable pivotTable) {
        CalculationOnPivot calculationOnPivot = new CalculationOnPivot();
        calculationOnPivot.doHorizontalCalculationOn(pivotTable, CalculationType.AVG);
    }

    private void sortHighToLowOnAverage(PivotTable pivotTable) {
        SortOnPivot sortOnPivot = new SortOnPivot();
        sortOnPivot.sortHighToLowOnAverage(pivotTable);
    }

    private void calculateTotalsPerYear(PivotTable pivotTable) {
        CalculationOnPivot calculationOnPivot = new CalculationOnPivot();
        calculationOnPivot.doVerticalCalculationOn(pivotTable, CalculationType.SUM);
    }

    private void selectTopX(PivotTable pivotTable, int i) {
        CalculationOnPivot calculationOnPivot = new CalculationOnPivot();
        calculationOnPivot.selectHorizontalTop(pivotTable, i);
    }

}
