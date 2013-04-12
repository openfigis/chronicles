package org.fao.fi.chronicles.web;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-chronicles.xml" })
public class ChroniclesControllerTest {

    private ChroniclesController chroniclesController;

    @Test
    public void testGenerateDevelopmentChart() throws IOException {

        File file = new File("ChroniclesControllerDevelopment.png");
        OutputStream o = new FileOutputStream(file);

        chroniclesController.generateDevelopmentChart(1970, 1980, 8, 20, o);

        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testGenerateCatchChart() throws IOException {
        File file = new File("ChroniclesControllerDevelopment.png");
        OutputStream o = new FileOutputStream(file);

        chroniclesController.generateCatchChart(1995, 1999, 101, o);

        assertTrue(file.exists());
        file.delete();
    }

    @Autowired
    public void setChroniclesController(ChroniclesController chroniclesController) {
        this.chroniclesController = chroniclesController;
    }

}
