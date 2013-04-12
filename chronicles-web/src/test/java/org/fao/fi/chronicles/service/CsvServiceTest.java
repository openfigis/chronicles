package org.fao.fi.chronicles.service;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.fao.fi.chronicles.domain.CatchParms;
import org.fao.fi.chronicles.domain.DevelopmentParmsMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-chronicles.xml" })
public class CsvServiceTest {

    CsvService csvService;

    @Test
    public void testTrack2OutputStream() throws FileNotFoundException {
        
        
        System.out.println(System.getProperty("java.io.tmpdir"));
        
        String piet = "piet.csv";
        File file = new File(piet);
        for (int i = 1; i < 7; i++) {
            FileOutputStream fos = new FileOutputStream(file);
            CatchParms p = DevelopmentParmsMock.mockIt();
            csvService.track2OutputStream(p, i, fos);
            assertTrue(file.exists());
            file.delete();
        }
    }

    @Autowired
    public void setCsvService(CsvService csvService) {
        this.csvService = csvService;
    }

}
