package org.fao.fi.chronicles.fishstatj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.fao.fi.tabularseries.metamodel.Concept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-chronicles-test.xml" })
public class FishstatProcessTest {

    FishstatProcess fishstatProcess;

    @Test
    public void testDummy() {
        assertTrue(true);
    }

    /**
     * This one takes a lot of time and needs as well extra Java memory. 256M is enough. -Xmx256M TODO Caused by:
     * java.sql.SQLSyntaxErrorException: Column 'ALPHA_3_UN_CODE' is either not in any table in the FROM list or appears
     * within a join specification and is outside the scope of the join specification or appears in a HAVING clause and
     * is not in the GROUP BY list. If this is a CREATE or ALTER TABLE statement then 'ALPHA_3_UN_CODE' is not a column
     * in the target table.
     */
    @Test
    public void testRun() {
        assertNotNull(fishstatProcess);

        FishstatProcessResult result = fishstatProcess.run(1960, 1965, null);
        String table[][] = result.getTabularSeries().getTable();

        // check whether all area numbers are below 10
        int areaIndex = 0;
        List<? extends Concept> list = result.getSeriesMetadata().getHeaderList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals("AREA")) {
                areaIndex = i;
            }
        }
        // Tabular2Csv.write2csv(table, "src/test/resources/FishstatJOutput.csv");

        for (String[] row : table) {
            if (!row[areaIndex].equals("AREA")) {
                int area = new Integer(row[areaIndex]);
                assertTrue(area > 10);
            }
            for (String field : row) {
                assertFalse(field.equals(""));
            }
        }
        assertTrue(table.length > 0);
        assertTrue(table[0].length > 0);

    }

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
    public void testRunCountries() {
        String cs[] = { "392" };// japan
        FishstatProcessResult result = fishstatProcess.run(1960, 1965, cs);
        System.out.println(result.getTabularSeries().getTable().length);
        assertEquals(2731, result.getTabularSeries().getTable().length, 500);
        result.getTabularSeries().getTable();
    }

    @Test
    public void testRunCountriesAzerbaijan() {
        String cs[] = { "031" };// Azerbaijan
        fishstatProcess.run(1950, 2007, cs);
    }

    @Autowired
    public void setFishstatProcess(FishstatProcess fishstatProcess) {
        this.fishstatProcess = fishstatProcess;
    }

}
