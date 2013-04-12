package org.fao.fi.pivot.calculation;

import static org.junit.Assert.assertEquals;

import org.fao.fi.pivot.model.CalculatedField;
import org.junit.Test;

public class DoubleValueComperatorTest {

    @Test
    public void testCompare() {
        CalculatedField d = new CalculatedField();
        d.setValue(10.0);
        CalculatedField f = new CalculatedField();
        f.setValue(15.0);
        CalculatedField g = new CalculatedField();
        g.setValue(5.0);
        DoubleValueComperator c = new DoubleValueComperator();
        assertEquals(0, c.compare(d, d));
        assertEquals(1, c.compare(d, f));
        assertEquals(-1, c.compare(d, g));
    }

}
