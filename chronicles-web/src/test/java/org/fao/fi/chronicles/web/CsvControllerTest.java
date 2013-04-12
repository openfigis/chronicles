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
public class CsvControllerTest {

    CsvController csvController;

    @Test
    public void generateDevelopmentCsv1() throws IOException {
        for (int i = 1; i < 7; i++) {
            File file = new File("ChroniclesControllerDevelopment.csv");
            OutputStream o = new FileOutputStream(file);
            csvController.generateDevelopmentCsv(i, 1980, 1985, 8, 10, o);
            assertTrue(file.exists());
            file.delete();
        }

    }

    @Test
    public void generateDevelopmentChart2() throws IOException {
        for (int i = 1; i < 7; i++) {
            File file = new File("ChroniclesControllerDevelopment.csv");
            OutputStream outputStream = new FileOutputStream(file);
            csvController.generateDevelopmentChart(i, 1950, 2007, 10, 200, "156", outputStream);
            assertTrue(file.exists());
            file.delete();
        }

    }

    @Autowired
    public void setCsvController(CsvController csvController) {
        this.csvController = csvController;
    }

}
