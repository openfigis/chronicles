package org.fao.fi.chronicles.fishstatj;

import org.fao.fi.tabulardata.model.TabularSeries;
import org.fao.fi.tabularseries.metamodel.SeriesMetadata;

/**
 * 
 * From the FishstatJ api a result is extracted in the format of a timeseries. The seriesMetadata explains how the
 * series is structured.
 * 
 * 
 * @author Erik van Ingen
 * 
 */

public class FishstatProcessResult {

    private TabularSeries tabularSeries;
    private SeriesMetadata seriesMetadata;

    public TabularSeries getTabularSeries() {
        return tabularSeries;
    }

    public void setTabularSeries(TabularSeries tabularSeries) {
        this.tabularSeries = tabularSeries;
    }

    public SeriesMetadata getSeriesMetadata() {
        return seriesMetadata;
    }

    public void setSeriesMetadata(SeriesMetadata seriesMetadata) {
        this.seriesMetadata = seriesMetadata;
    }

}
