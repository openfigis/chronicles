package org.fao.fi.chronicles.chartcreation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnCountryCodeComposerTest {

    @Test
    public void testComposeName() {
        String un1[] = { "392" };
        assertEquals("Japan", UnCountryCodeComposer.composeName(un1));

        String un2[] = { "392", "032" };
        assertEquals("Japan and Argentina", UnCountryCodeComposer.composeName(un2));

        String un3[] = { "392", "032",  "392" };
        assertEquals("Japan, Argentina and Japan", UnCountryCodeComposer.composeName(un3));
    
    }

}
