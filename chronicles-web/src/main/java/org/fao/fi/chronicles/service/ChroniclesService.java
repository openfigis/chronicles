package org.fao.fi.chronicles.service;

import org.fao.fi.chronicles.container.ChroniclesContainer;
import org.fao.fi.chronicles.domain.CatchParms;
import org.fao.fi.chronicles.domain.DevelopmentParms;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;

@Service
public class ChroniclesService {
    ChroniclesContainer chroniclesContainer;

    @Cacheable(cacheName = "ChroniclesCache")
    public JFreeChart produceCatchChart(CatchParms p) {
        return chroniclesContainer.produceCatchChart(p);
    }

    @Cacheable(cacheName = "ChroniclesCache")
    public JFreeChart produceDevelopmentChart(DevelopmentParms p) {
        return chroniclesContainer.produceDevelopmentChart(p);
    }

    @Autowired
    public void setChroniclesContainer(ChroniclesContainer chroniclesContainer) {
        this.chroniclesContainer = chroniclesContainer;
    }

}
