package org.fao.fi.chronicles.service;

import java.io.OutputStream;

import org.fao.fi.chronicles.container.Track;
import org.fao.fi.chronicles.container.TrackMachine;
import org.fao.fi.chronicles.domain.CatchParms;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.tabular2pivot.Pivot2Csv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CsvService {

    private TrackMachine trackMachine;

    public void track2OutputStream(CatchParms p, int track, OutputStream outputStream) {
        PivotTable pivotTable = trackMachine.run(p, track, Track.DEVELOPMENT);
        Pivot2Csv.write2OutputStream(pivotTable, outputStream);
    }

   

    public void track2CaptureOutputStream(CatchParms p, int track, OutputStream outputStream) {
        PivotTable pivotTable = trackMachine.run(p, track, Track.DEVELOPMENT);
        Pivot2Csv.write2OutputStream(pivotTable, outputStream);
    }

   @Autowired
    public void setTrackMachine(TrackMachine trackMachine) {
        this.trackMachine = trackMachine;
    }


}
