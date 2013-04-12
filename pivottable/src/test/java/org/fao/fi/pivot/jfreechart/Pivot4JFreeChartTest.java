package org.fao.fi.pivot.jfreechart;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.fao.fi.pivot.calculation.CalculationOnPivot;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.data.category.CategoryDataset;
import org.junit.Test;

public class Pivot4JFreeChartTest {

    /**
     * jfreechart double value = pivottable Fact
     * 
     * jfreechart Comparable rowKey = pivottable pivottable rowfield name
     * 
     * jfreechart Comparable columnKey) = pivottable columnfield name
     */
    @Test
    public void testCreateCategoryDataset() {
        int rows = 11;
        int columns = 3;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        CategoryDataset dataset4Jfreechart = Pivot4JFreeChart.createCategoryDataset(pivotTable, true);
        assertEquals(columns, dataset4Jfreechart.getRowCount());
        assertEquals(rows, dataset4Jfreechart.getColumnCount());
    }

    @Test
    public void testCategoryDataset2Chart() throws IOException {
        int rows = 10;
        int columns = 12;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);
        Pivot2Sysout.pivot2SysOut(pivotTable);
        CategoryDataset dataset4Jfreechart = Pivot4JFreeChart.createCategoryDataset(pivotTable, true);
        JFreeChart chart = Pivot4JFreeChart.categoryDataset2StackedBarChart3D(ChartTextMock.mockIt(),
                dataset4Jfreechart);
        ChartRenderingInfo chartrenderinginfo = new ChartRenderingInfo(new StandardEntityCollection());
        File file = new File("ImageMapDemo7.png");
        ChartUtilities.saveChartAsPNG(file, chart, 600, 400, chartrenderinginfo);
        file.delete();
    }

    @Test
    public void testCreateTopOthers() {
        int rows = 11;
        int columns = 2;
        int numberOfSelectedItems = 3;
        PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, 1);

        CalculationOnPivot c = new CalculationOnPivot();
        c.selectHorizontalTop(pivotTable, numberOfSelectedItems);

        Pivot2Sysout.pivot2SysOut(pivotTable);
        CategoryDataset dataset4Jfreechart = Pivot4JFreeChart.createCategoryDataset(pivotTable, true);
        assertEquals(columns, dataset4Jfreechart.getRowCount());
        // other is added
        assertEquals(++numberOfSelectedItems, dataset4Jfreechart.getColumnCount());
    }

}
