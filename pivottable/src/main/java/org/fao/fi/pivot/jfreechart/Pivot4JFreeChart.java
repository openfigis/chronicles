package org.fao.fi.pivot.jfreechart;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYSeries;

/**
 * Functions for extracting information from a pivot table for the jfreechart.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class Pivot4JFreeChart {

    private Pivot4JFreeChart() {
        // Utility classes should not have a public or default constructor
    }

    /**
     * Get the values from the pivottable and create a dataset for jfreechart.
     * 
     * jfreechart double value = pivottable Fact
     * 
     * jfreechart Comparable rowKey = pivottable columnfield name
     * 
     * jfreechart Comparable columnKey) = pivottable pivottable rowfield name
     * 
     * 
     * @param pivotTable
     * @return
     */
    public static CategoryDataset createCategoryDataset(PivotTable pivotTable, boolean transposed) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<ColumnField> columns = pivotTable.getColumnSection().getColumnFieldList();
        List<Row> rows = pivotTable.getRowSection().getRowList();
        for (ColumnField columnField : columns) {
            List<AbstractFact> facts = columnField.getFactList();
            for (AbstractFact abstractFact : facts) {
                Row row = rows.get(facts.indexOf(abstractFact));
                Fact fact = (Fact) abstractFact;
                if (transposed) {
                    dataset.addValue(fact.getValue(), columnField.getValue(), row.getRowFieldList().get(0).getValue());
                } else {
                    dataset.addValue(fact.getValue(), row.getRowFieldList().get(0).getValue(), columnField.getValue());
                }
            }
        }
        return dataset;
    }

    public static JFreeChart categoryDataset2StackedBarChart3D(ChartText chartText, CategoryDataset categoryDataset) {
        JFreeChart jfreechart = ChartFactory.createStackedBarChart(chartText.getTitle(), chartText.getxAxisTitle(),
                chartText.getyAxisTitle(), categoryDataset, PlotOrientation.VERTICAL, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);
        CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
        categoryplot.setBackgroundPaint(Color.lightGray);
        categoryplot.setRangeGridlinePaint(Color.white);
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        StackedBarRenderer stackedbarrenderer = (StackedBarRenderer) categoryplot.getRenderer();
        stackedbarrenderer.setRenderAsPercentages(true);
        stackedbarrenderer.setDrawBarOutline(false);
        stackedbarrenderer.setBarPainter( new StandardBarPainter());
        stackedbarrenderer.setShadowVisible(false);
        categoryplot.getDomainAxis().setMaximumCategoryLabelLines(2);
        return jfreechart;
    }

    public static TableXYDataset createTableXYDataset(PivotTable pivotTable) {
        DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        List<Row> rows = pivotTable.getRowSection().getRowList();
        for (Row row : rows) {
            XYSeries xyseries = new XYSeries(row.getRowFieldList().get(0).getValue(), false, false);
            List<AbstractFact> factList = row.getFactList();
            for (AbstractFact abstractFact : factList) {
                ColumnField columnField = pivotTable.getColumnSection().getColumnFieldList()
                        .get(factList.indexOf(abstractFact));
                columnField.getValue();
                Double dValue = new Double(columnField.getValue());
                Fact fact = (Fact) abstractFact;
                xyseries.add(dValue.doubleValue(), fact.getValue() / 1e6);
            }
            dataset.addSeries(xyseries);
        }
        return dataset;
    }

    public static JFreeChart createStackedXYAreaChart(ChartText chartText, TableXYDataset tablexydataset) {
        JFreeChart jfreechart = ChartFactory.createStackedXYAreaChart(chartText.getTitle(), chartText.getxAxisTitle(),
                chartText.getyAxisTitle(), tablexydataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
        numberaxis.setNumberFormatOverride(new DecimalFormat("0000"));
        StackedXYAreaRenderer stackedxyarearenderer = new StackedXYAreaRenderer();
        stackedxyarearenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        xyplot.setRenderer(0, stackedxyarearenderer);
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);
        return jfreechart;
    }

}
