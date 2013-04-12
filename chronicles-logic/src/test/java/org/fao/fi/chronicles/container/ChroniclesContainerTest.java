package org.fao.fi.chronicles.container;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.fao.fi.chronicles.calcuation.FishstatProcessTestHelper;
import org.fao.fi.chronicles.domain.DevelopmentParms;
import org.fao.fi.chronicles.domain.DevelopmentParmsMock;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-chronicles-test.xml" })
public class ChroniclesContainerTest {

    ChroniclesContainer chroniclesContainer;

    
    
    @Before
    public void before() {
        chroniclesContainer.setFishstatProcess(new FishstatProcessTestHelper());
    }
    
    
    
    @Test
    public void testProduceChart() throws IOException {

        File file = new File("ChroniclesContainerTest.png");
        OutputStream o = new FileOutputStream(file);
        DevelopmentParms p = DevelopmentParmsMock.mockIt();
        JFreeChart chart = chroniclesContainer.produceDevelopmentChart(p);
        ChartUtilities.writeChartAsPNG(o, chart, 750, 400);

        assertTrue(file.exists());
        file.delete();
    }

    @Autowired
    public void setChroniclesContainer(ChroniclesContainer chroniclesContainer) {
        this.chroniclesContainer = chroniclesContainer;
    }

}
