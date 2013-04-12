package org.fao.fi.chronicles.chartcreation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fao.fi.pivot.jfreechart.ChartText;
import org.fao.fi.pivot.jfreechart.Pivot4JFreeChart;
import org.fao.fi.pivot.logic.Phase;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;

public class DevChartCreationProcess {

    public static Map<Phase, String> symbolName = new HashMap<Phase, String>();
    static {
        symbolName.put(Phase.D, "Phase 2 Developing");
        symbolName.put(Phase.M, "Phase 3 Mature");
        symbolName.put(Phase.R, "Phase 5 Recovering");
        symbolName.put(Phase.S, "Phase 4 Senescent");
        symbolName.put(Phase.U, "Phase 1 Undeveloped");
    }

    public JFreeChart run(PivotTable pivotTable, String[] un) {
        adjustCategoryNames(pivotTable);
        CategoryDataset categoryDataset = createCategoryDataset(pivotTable, true);
        return createStackedBarChart3D(categoryDataset, un);
    }

    private void adjustCategoryNames(PivotTable pivotTable) {
        List<ColumnField> categories = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : categories) {
            Phase phase = Phase.valueOf(columnField.getValue());
            columnField.setValue(symbolName.get(phase));
        }

    }

    private JFreeChart createStackedBarChart3D(CategoryDataset categoryDataset, String[] un) {
        ChartText c = new ChartText();
        String title;
        if (un == null) {
            title = "Percentage of the world's top fishery resources in various phases of fishery development";
        } else {
            String countryNames = UnCountryCodeComposer.composeName(un);
            title = "Percentage of the top fishery resources in various phases of fishery development in "
                    + countryNames;
        }
        c.setTitle(title);
        c.setxAxisTitle("Period");
        c.setyAxisTitle("Percentage");
        return Pivot4JFreeChart.categoryDataset2StackedBarChart3D(c, categoryDataset);
    }

    private CategoryDataset createCategoryDataset(PivotTable pivotTable, boolean transposed) {
        return Pivot4JFreeChart.createCategoryDataset(pivotTable, transposed);
    }

}
