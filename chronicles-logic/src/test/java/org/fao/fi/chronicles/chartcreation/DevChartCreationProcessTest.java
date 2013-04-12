package org.fao.fi.chronicles.chartcreation;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.tabular2pivot.Csv2Pivot;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.junit.Test;

public class DevChartCreationProcessTest {

    PivotTableValidator v = new PivotTableValidator();

    @Test
    public void testRunDevelopment() throws IOException {
        PivotTable pivotTable = Csv2Pivot
                .createPivotTableFromCsv("src/test/resources/inputForChartCreation/CalculationProcess3Test.csv");
        assertTrue(v.validate(pivotTable));
        File file = new File("Development.png");
        OutputStream o = new FileOutputStream(file);

        DevChartCreationProcess c = new DevChartCreationProcess();
        String[] un = { "392", "032" };
        JFreeChart chart = c.run(pivotTable, un);
        ChartUtilities.writeChartAsPNG(o, chart, 750, 400);

        assertTrue(file.exists());
        file.delete();
    }


}
