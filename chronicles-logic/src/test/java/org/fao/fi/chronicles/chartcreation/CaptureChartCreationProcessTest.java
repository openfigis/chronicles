package org.fao.fi.chronicles.chartcreation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static org.junit.Assert.assertTrue;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.tabular2pivot.Csv2Pivot;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.junit.Test;

public class CaptureChartCreationProcessTest {

    PivotTableValidator v = new PivotTableValidator();

    @Test
    public void testRunCatch() throws IOException {
        PivotTable pivotTable = Csv2Pivot
                .createPivotTableFromCsv("src/test/resources/inputForChartCreation/CatchCalculationProcess2Test.csv");
        assertTrue(v.validate(pivotTable));
        File file = new File("Catch.png");
        OutputStream o = new FileOutputStream(file);

        CaptureChartCreationProcess c = new CaptureChartCreationProcess();
        JFreeChart chart = c.run(pivotTable, null);
        ChartUtilities.writeChartAsPNG(o, chart, 750, 400);

        assertTrue(file.exists());
        file.delete();
    }

}
