package org.fao.fi.chronicles.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

public class DevelopmentParmsTest {

    @Test
    public void testEqualsObject() {
        DevelopmentParms d1 = DevelopmentParmsMock.mockIt();
        DevelopmentParms d2 = DevelopmentParmsMock.mockIt();
        assertEquals(d1, d2);
        String a[] = { "444", "444", "444" };
        d2.setUn(a);
        assertNotSame(d1, d2);
    }

}
