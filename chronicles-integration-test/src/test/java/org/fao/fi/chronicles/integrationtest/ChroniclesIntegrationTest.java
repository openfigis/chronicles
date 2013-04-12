package org.fao.fi.chronicles.integrationtest;

import static org.junit.Assert.assertNotNull;

import org.fao.fi.commons.integrationtest.tools.RestWebserviceIntegrationTest;
import org.junit.Test;

public class ChroniclesIntegrationTest {

    RestWebserviceIntegrationTest test = new RestWebserviceIntegrationTest("http://figis02:8888/chronicles-web/",
            "http://localhost:8080/chronicles-web/");

    /**
     * development chart
     * 
     */
    @Test
    public void chartDevelopmentStartyear1950endyear2007Clusters10top200() throws Exception {
        assertNotNull(test.doGetOn("chart/development/startyear/1950/endyear/2007/clusters/10/top/200/chart.png"));
    }

    /**
     * development chart for japan
     * 
     */
    @Test
    public void chartDevelopmentStartyear1950endyear2007clusters10top200un392() throws Exception {
        assertNotNull(test
                .doGetOn("chart/development/startyear/1950/endyear/2007/clusters/10/top/200/un/392/chart.png"));
    }

    /**
     * development chart for Azerbaijan
     * 
     */
    @Test
    public void chartDevelopmentStartyear1950endyear2007clusters10top200un036Azerbaijan() throws Exception {
        assertNotNull(test
                .doGetOn("chart/development/startyear/1950/endyear/2007/clusters/10/top/200/un/031/chart.png"));
    }

    /**
     * capture chart
     * 
     */
    @Test
    public void chartCaptureStartyear1950endyear2007top200() throws Exception {
        assertNotNull(test.doGetOn("chart/capture/startyear/1950/endyear/2007/top/200/chart.png"));
    }

    /**
     * capture chart for japan and argentina
     * 
     */
    @Test
    public void chartCaptureStartyear1950endyear2007top200un392032() throws Exception {
        assertNotNull(test.doGetOn("chart/capture/startyear/1950/endyear/2007/top/200/un/392032/chart.png"));
    }

    /**
     * capture chart for Azerbaijan
     * 
     */
    @Test
    public void chartCaptureStartyear1950endyear2007top200un036Azerbaijan() throws Exception {
        assertNotNull(test.doGetOn("chart/capture/startyear/1950/endyear/2007/top/200/un/031/chart.png"));
    }

    /**
     * capture csv
     * 
     */
    @Test
    public void csvCaptureTrack1Startyear1995Endyear1999Top10CaptureCsv() throws Exception {
        for (int i = 1; i < 5; i++) {
            assertNotNull(test.doGetOn("csv/capture/track/" + i + "/startyear/1950/endyear/2007/top/200/capture.csv"));
        }
    }

    @Test
    public void csvCaptureTrack1Startyear1995Endyear1999Top10Un352CaptureCsv() throws Exception {
        for (int i = 1; i < 5; i++) {
            assertNotNull(test.doGetOn("csv/capture/track/" + i
                    + "/startyear/1950/endyear/2007/top/200/un/392032/capture.csv"));
        }
    }

}
