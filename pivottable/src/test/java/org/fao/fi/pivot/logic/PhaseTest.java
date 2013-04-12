package org.fao.fi.pivot.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PhaseTest {

    @Test
    public void testValueOf() {

        assertEquals(Phase.M, Phase.valueOf("M"));
        assertEquals(Phase.S, Phase.valueOf("S"));

    }

}
