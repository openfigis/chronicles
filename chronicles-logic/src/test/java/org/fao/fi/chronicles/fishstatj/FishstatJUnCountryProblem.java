package org.fao.fi.chronicles.fishstatj;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-chronicles-test.xml" })
public class FishstatJUnCountryProblem {

    FishstatProcess fishstatProcess;

    /**
     * This one takes a lot of time and needs as well extra Java memory. 256M is enough. -Xmx256M
     * 
     * TODO Caused by: java.sql.SQLException: Column 'ALPHA_3_UN_CODE' is either not in any table in the FROM list or
     * appears within a join specification and is outside the scope of the join specification or appears in a HAVING
     * clause and is not in the GROUP BY list. If this is a CREATE or ALTER TABLE statement then 'ALPHA_3_UN_CODE' is
     * not a column in the target table.
     * 
     */
    @Test
    public void testRunCountryJapan() {
        String cs[] = { "392" };// japan
        FishstatProcessResult result = fishstatProcess.run(1960, 1965, cs);
        System.out.println(result.getTabularSeries().getTable().length);
        assertEquals(2731, result.getTabularSeries().getTable().length, 500);
        result.getTabularSeries().getTable();
    }

    //@Test
    public void testRunCountryJapanArgentia() {
        String cs[] = { "392", "032" };// japan and argentina
        FishstatProcessResult result = fishstatProcess.run(1960, 1965, cs);
        System.out.println(result.getTabularSeries().getTable().length);
        assertEquals(2731, result.getTabularSeries().getTable().length, 500);
        result.getTabularSeries().getTable();
    }

    @Autowired
    public void setFishstatProcess(FishstatProcess fishstatProcess) {
        this.fishstatProcess = fishstatProcess;
    }

}
