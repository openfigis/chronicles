package org.fao.fi.chronicles.container;

import org.fao.fi.chronicles.calcuation.FishstatProcessTestHelper;
import org.fao.fi.chronicles.domain.DevelopmentParms;
import org.fao.fi.chronicles.domain.DevelopmentParmsMock;
import org.fao.fi.chronicles.fishstatj.FishstatProcess;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-chronicles-test.xml" })
public class TrackMachineTest extends FishstatProcessTestHelper {

    private TrackMachine trackMachine;
    FishstatProcess fishstatProcessTestHelper;

    @Before
    public void before() {
        trackMachine.setFishstatProcess(new FishstatProcessTestHelper());
    }

    @Test
    public void testRun() {
        DevelopmentParms p = DevelopmentParmsMock.mockIt();
        trackMachine.run(p, 6, Track.DEVELOPMENT);
    }

    @Test
    public void testLongRun() {
        DevelopmentParms p = DevelopmentParmsMock.mockIt();
        for (int track = 1; track < 7; track++) {
            System.out.println("testRun:" + track);
            trackMachine.run(p, track, Track.DEVELOPMENT);
        }
    }

    @Test
    public void testRunDevelopment() {
        DevelopmentParms p = DevelopmentParmsMock.mockIt();
        for (int track = 1; track < 5; track++) {
            System.out.println("testRun:" + track);
            trackMachine.run(p, track, Track.DEVELOPMENT);
        }
    }

    @Autowired
    public void setTrackMachine(TrackMachine trackMachine) {
        this.trackMachine = trackMachine;
    }

    @Autowired
    public void setFishstatProcessTestHelper(FishstatProcess fishstatProcessTestHelper) {
        this.fishstatProcessTestHelper = fishstatProcessTestHelper;
    }

}
