package org.fao.fi.pivot.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClassificationTest {

    @Test
    public void testDeterminePhase() {
        Classification c = new Classification();
        NumericPhase up = NumericPhase.up;
        NumericPhase equal = NumericPhase.equal;
        NumericPhase down = NumericPhase.down;
        assertEquals(Phase.S, c.determinePhase(down));
        assertEquals(Phase.U, c.determinePhase(equal));
        assertEquals(Phase.D, c.determinePhase(up));
        assertEquals(Phase.M, c.determinePhase(equal));
        assertEquals(Phase.S, c.determinePhase(down));
        assertEquals(Phase.R, c.determinePhase(up));
        assertEquals(Phase.M, c.determinePhase(equal));
        assertEquals(Phase.M, c.determinePhase(equal));
        assertEquals(Phase.R, c.determinePhase(up));
    }

}
