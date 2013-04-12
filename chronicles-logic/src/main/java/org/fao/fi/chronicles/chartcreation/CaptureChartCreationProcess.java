package org.fao.fi.chronicles.chartcreation;

import java.util.HashMap;
import java.util.Map;

import org.fao.fi.pivot.jfreechart.ChartText;
import org.fao.fi.pivot.jfreechart.Pivot4JFreeChart;
import org.fao.fi.pivot.logic.Phase;
import org.fao.fi.pivot.model.PivotTable;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.TableXYDataset;

public class CaptureChartCreationProcess {

    public static Map<Phase, String> symbolName = new HashMap<Phase, String>();
    static {
        symbolName.put(Phase.D, "Phase 2 Developing");
        symbolName.put(Phase.M, "Phase 3 Mature");
        symbolName.put(Phase.R, "Phase 5 Recovering");
        symbolName.put(Phase.S, "Phase 4 Senescent");
        symbolName.put(Phase.U, "Phase 1 Undeveloped");
    }

 
    public JFreeChart run(PivotTable pivotTable, String[] un) {
        TableXYDataset tableXYDataset = createTableXYDataset(pivotTable);
        return createCategoryDataset2StackedAreaChart(tableXYDataset, un);
    }

    private TableXYDataset createTableXYDataset(PivotTable pivotTable) {
        return Pivot4JFreeChart.createTableXYDataset(pivotTable);

    }
    private JFreeChart createCategoryDataset2StackedAreaChart(TableXYDataset tableXYDataset, String[] un) {
        String title;
        if (un == null) {
            title = "Total capture";
        } else {
            String countryNames = UnCountryCodeComposer.composeName(un);
            title = "Total capture in " + countryNames;
        }
        ChartText c = new ChartText();
        c.setTitle(title);
        c.setxAxisTitle("Year");
        c.setyAxisTitle("Landings (million tonnes)");
        return Pivot4JFreeChart.createStackedXYAreaChart(c, tableXYDataset);
    }

}
