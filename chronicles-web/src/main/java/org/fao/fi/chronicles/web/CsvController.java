package org.fao.fi.chronicles.web;

import java.io.IOException;
import java.io.OutputStream;

import org.fao.fi.chronicles.domain.CatchParms;
import org.fao.fi.chronicles.domain.DevelopmentParms;
import org.fao.fi.chronicles.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The controller for the chronicles webservice, producing the charts.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
@Controller
@RequestMapping
public class CsvController {

    private CsvService csvService;

    /**
     * Generate developement csv
     * 
     * 
     * @param track
     * @param startYear
     * @param endYear
     * @param clusters
     * @param top
     * @param outputStream
     * @throws IOException
     */
    @RequestMapping(value = "/development/track/{track}/startyear/{startyear}/endyear/{endyear}/clusters/{clusters}/top/{top}/development.csv", method = RequestMethod.GET)
    @ResponseBody
    public final void generateDevelopmentCsv(@PathVariable("track") int track,
            @PathVariable("startyear") int startYear, @PathVariable("endyear") int endYear,
            @PathVariable("clusters") int clusters, @PathVariable("top") int top, OutputStream outputStream)
            throws IOException {

        // set the params in the parameter object
        DevelopmentParms p = new DevelopmentParms();
        p.setClusters(clusters);
        p.setEndYear(endYear);
        p.setStartYear(startYear);
        p.setTop(top);
        csvService.track2OutputStream(p, track, outputStream);
    }

    /**
     * Generate development csv, specifying the country(ies)
     * 
     * @param track
     * @param startYear
     * @param endYear
     * @param clusters
     * @param top
     * @param un
     * @param outputStream
     * @throws IOException
     */
    @RequestMapping(value = "/development/track/{track}/startyear/{startyear}/endyear/{endyear}/clusters/{clusters}/top/{top}/un/{un}/development.csv", method = RequestMethod.GET)
    @ResponseBody
    public final void generateDevelopmentChart(@PathVariable("track") int track,
            @PathVariable("startyear") int startYear, @PathVariable("endyear") int endYear,
            @PathVariable("clusters") int clusters, @PathVariable("top") int top, @PathVariable("un") String un,
            OutputStream outputStream) throws IOException {
        // set the params in the parameter object
        DevelopmentParms p = new DevelopmentParms();
        p.setClusters(clusters);
        p.setEndYear(endYear);
        p.setStartYear(startYear);
        p.setTop(top);
        p.setUn(UnCodeInterpreter.parse(un));

        csvService.track2OutputStream(p, track, outputStream);
    }

    /**
     * Generating the csv for capture data
     * 
     * @param track
     * @param startYear
     * @param endYear
     * @param top
     * @param outputStream
     * @throws IOException
     */
    @RequestMapping(value = "/capture/track/{track}/startyear/{startyear}/endyear/{endyear}/top/{top}/capture.csv", method = RequestMethod.GET)
    @ResponseBody
    public final void generateCatchChart(@PathVariable("track") int track, @PathVariable("startyear") int startYear,
            @PathVariable("endyear") int endYear, @PathVariable("top") int top, OutputStream outputStream)
            throws IOException {

        // set the params in the parameter object
        CatchParms p = new CatchParms();
        p.setEndYear(endYear);
        p.setStartYear(startYear);
        p.setTop(top);
        csvService.track2CaptureOutputStream(p, track, outputStream);
    }

    /**
     * 
     * Generating the csv for capture data, specifying the country(ies)
     * 
     * @param track
     * @param startYear
     * @param endYear
     * @param top
     * @param un
     * @param outputStream
     * @throws IOException
     */
    @RequestMapping(value = "/capture/track/{track}/startyear/{startyear}/endyear/{endyear}/top/{top}/un/{un}/capture.csv", method = RequestMethod.GET)
    @ResponseBody
    public final void generateCatchChart(@PathVariable("track") int track, @PathVariable("startyear") int startYear,
            @PathVariable("endyear") int endYear, @PathVariable("top") int top, @PathVariable("un") String un,
            OutputStream outputStream) throws IOException {

        // set the params in the parameter object
        CatchParms p = new CatchParms();
        p.setEndYear(endYear);
        p.setStartYear(startYear);
        p.setTop(top);
        p.setUn(UnCodeInterpreter.parse(un));
        csvService.track2CaptureOutputStream(p, track, outputStream);
    }

    @Autowired
    public void setCsvService(CsvService csvService) {
        this.csvService = csvService;
    }

}
