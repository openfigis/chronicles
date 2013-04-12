package org.fao.fi.chronicles.container;

import org.fao.fi.chronicles.aesthetic.AestheticProcess;
import org.fao.fi.chronicles.calcuation.CalculationProcess1;
import org.fao.fi.chronicles.calcuation.CalculationProcess2;
import org.fao.fi.chronicles.calcuation.CalculationProcess3;
import org.fao.fi.chronicles.calcuation.CatchCalculationProcess2;
import org.fao.fi.chronicles.chartcreation.CaptureChartCreationProcess;
import org.fao.fi.chronicles.chartcreation.DevChartCreationProcess;
import org.fao.fi.chronicles.domain.CatchParms;
import org.fao.fi.chronicles.domain.DevelopmentParms;
import org.fao.fi.chronicles.fishstatj.FishstatProcess;
import org.fao.fi.chronicles.fishstatj.FishstatProcessResult;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.tabular2pivot.TabularSeries2Pivot;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This is the main class of this component. The Process will use the fishstatJ api to retrieve the data, do
 * calculations and produce the charts.
 * 
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
@Component
public class ChroniclesContainer {

    private FishstatProcess fishstatProcess;
    private CalculationProcess1 calculationProcess1 = new CalculationProcess1();
    private AestheticProcess aestheticProcess = new AestheticProcess();
    private CalculationProcess2 calculationProcess2 = new CalculationProcess2();
    private CalculationProcess3 calculationProcess3 = new CalculationProcess3();

    private CatchCalculationProcess2 catchCalculationProcess2 = new CatchCalculationProcess2();

    private DevChartCreationProcess devChartCreationProcess = new DevChartCreationProcess();
    private CaptureChartCreationProcess captureChartCreationProcess = new CaptureChartCreationProcess();

    public JFreeChart produceDevelopmentChart(DevelopmentParms p) {

        // do the fishstatJ stuff -track1
        FishstatProcessResult r = fishstatProcess.run(p.getStartYear(), p.getEndYear(), p.getUn());

        // convert tabular series data to a pivot table
        TabularSeries2Pivot tabularSeries2Pivot = new TabularSeries2Pivot(r.getSeriesMetadata());
        PivotTable pivotTable = tabularSeries2Pivot.convert(r.getTabularSeries());

        // do all necessary calculations -track2
        calculationProcess1.run(pivotTable, p.getTop());

        // do some aesthetic changes -track3
        aestheticProcess.run(pivotTable);

        // do the cluster analyses -track4
        calculationProcess2.run(pivotTable, p.getClusters());

        // calculate the slopes and phases. -track 5
        pivotTable = calculationProcess3.run(pivotTable);

        // generate the chart -track 6
        return devChartCreationProcess.run(pivotTable, p.getUn());

    }

    public JFreeChart produceCatchChart(CatchParms p) {

        // do the fishstatJ stuff -track1
        FishstatProcessResult r = fishstatProcess.run(p.getStartYear(), p.getEndYear(), p.getUn());

        // convert tabular series data to a pivot table
        TabularSeries2Pivot tabularSeries2Pivot = new TabularSeries2Pivot(r.getSeriesMetadata());
        PivotTable pivotTable = tabularSeries2Pivot.convert(r.getTabularSeries());

        // do all necessary calculations -track2
        calculationProcess1.run(pivotTable, p.getTop());

        // do the catchy stuff -track 3
        catchCalculationProcess2.run(pivotTable);

        // generate the chart -track 4
        return captureChartCreationProcess.run(pivotTable, p.getUn());

    }

    @Autowired
    public void setFishstatProcess(FishstatProcess fishstatProcess) {
        this.fishstatProcess = fishstatProcess;
    }

}
