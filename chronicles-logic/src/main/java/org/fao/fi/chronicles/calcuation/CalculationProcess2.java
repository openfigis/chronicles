package org.fao.fi.chronicles.calcuation;

import org.fao.fi.pivot.calculation.CalculationOnPivot;
import org.fao.fi.pivot.calculation.ClusterAnalysisOnPivot;
import org.fao.fi.pivot.model.PivotTable;
import org.springframework.stereotype.Component;

@Component
public class CalculationProcess2 {

    public void run(PivotTable pivotTable, int clusters) {

        // standardise
        performStandardization(pivotTable);

        // do the cluster analysis
        performClusterAnalysis(pivotTable, clusters);
    }

    /**
     * mean 0, stdev 1
     * 
     * @param pivotTable
     */
    private void performStandardization(PivotTable pivotTable) {
        CalculationOnPivot c = new CalculationOnPivot();
        c.standardizeVerticalValues(pivotTable);
    }

    private void performClusterAnalysis(PivotTable pivotTable, int clusters) {
        // 5 columns cannot be clustered into 200 clusters
        if (clusters > pivotTable.getColumnSection().getColumnFieldList().size()) {
            clusters = pivotTable.getColumnSection().getColumnFieldList().size();
        }
        ClusterAnalysisOnPivot.cluster(pivotTable, clusters);
    }

}
