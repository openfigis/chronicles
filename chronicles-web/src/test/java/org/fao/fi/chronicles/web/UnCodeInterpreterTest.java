package org.fao.fi.chronicles.web;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnCodeInterpreterTest {

    
    /**
     * test with more cases
     */
    
    @Test
    public void testParse() {
        String a [] = UnCodeInterpreter.parse("123456");
        assertEquals(2, a.length);
        assertEquals("123", a[0]);
        assertEquals("456", a[1]);

        String b [] = UnCodeInterpreter.parse("123");
        assertEquals(1, b.length);
        assertEquals("123", b[0]);

        String c [] = UnCodeInterpreter.parse("032");
        assertEquals(1, c.length);
        assertEquals("032", c[0]);

        String d [] = UnCodeInterpreter.parse("032032");
        assertEquals(2, d.length);
        assertEquals("032", d[0]);
        assertEquals("032", d[1]);

    
    }

}
