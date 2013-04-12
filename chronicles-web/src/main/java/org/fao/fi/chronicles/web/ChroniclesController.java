package org.fao.fi.chronicles.web;

import java.io.IOException;
import java.io.OutputStream;

import org.fao.fi.chronicles.domain.CatchParms;
import org.fao.fi.chronicles.domain.DevelopmentParms;
import org.fao.fi.chronicles.service.ChroniclesService;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
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
public class ChroniclesController {

    private ChroniclesService chroniclesService;

    @RequestMapping(value = "/development/startyear/{startyear}/endyear/{endyear}/clusters/{clusters}/top/{top}/chart.png", method = RequestMethod.GET)
    @ResponseBody
    public final void generateDevelopmentChart(@PathVariable("startyear") int startYear,
            @PathVariable("endyear") int endYear, @PathVariable("clusters") int clusters, @PathVariable("top") int top,
            OutputStream stream) throws IOException {

        // set the params in the parameter object
        DevelopmentParms p = new DevelopmentParms();
        p.setClusters(clusters);
        p.setEndYear(endYear);
        p.setStartYear(startYear);
        p.setTop(top);

        JFreeChart chart = chroniclesService.produceDevelopmentChart(p);
        ChartUtilities.writeChartAsPNG(stream, chart, 750, 400);
    }

    @RequestMapping(value = "/development/startyear/{startyear}/endyear/{endyear}/clusters/{clusters}/top/{top}/un/{un}/chart.png", method = RequestMethod.GET)
    @ResponseBody
    public final void generateDevelopmentChart(@PathVariable("startyear") int startYear,
            @PathVariable("endyear") int endYear, @PathVariable("clusters") int clusters, @PathVariable("top") int top,
            @PathVariable("un") String un, OutputStream stream) throws IOException {
        // set the params in the parameter object
        DevelopmentParms p = new DevelopmentParms();
        p.setClusters(clusters);
        p.setEndYear(endYear);
        p.setStartYear(startYear);
        p.setTop(top);
        p.setUn(UnCodeInterpreter.parse(un));

        JFreeChart chart = chroniclesService.produceDevelopmentChart(p);
        ChartUtilities.writeChartAsPNG(stream, chart, 750, 400);
    }

    @RequestMapping(value = "/capture/startyear/{startyear}/endyear/{endyear}/top/{top}/chart.png", method = RequestMethod.GET)
    @ResponseBody
    public final void generateCatchChart(@PathVariable("startyear") int startYear,
            @PathVariable("endyear") int endYear, @PathVariable("top") int top, OutputStream stream) throws IOException {

        // set the params in the parameter object
        CatchParms p = new CatchParms();
        p.setEndYear(endYear);
        p.setStartYear(startYear);
        p.setTop(top);

        JFreeChart chart = chroniclesService.produceCatchChart(p);
        ChartUtilities.writeChartAsPNG(stream, chart, 750, 400);
    }

    @RequestMapping(value = "/capture/startyear/{startyear}/endyear/{endyear}/top/{top}/un/{un}/chart.png", method = RequestMethod.GET)
    @ResponseBody
    public final void generateCatchChart(@PathVariable("startyear") int startYear,
            @PathVariable("endyear") int endYear, @PathVariable("top") int top, @PathVariable("un") String un,
            OutputStream stream) throws IOException {

        // set the params in the parameter object
        CatchParms p = new CatchParms();
        p.setEndYear(endYear);
        p.setStartYear(startYear);
        p.setTop(top);
        p.setUn(UnCodeInterpreter.parse(un));
        JFreeChart chart = chroniclesService.produceCatchChart(p);
        ChartUtilities.writeChartAsPNG(stream, chart, 750, 400);
    }

    @Autowired
    public void setChroniclesService(ChroniclesService chroniclesService) {
        this.chroniclesService = chroniclesService;
    }

}
