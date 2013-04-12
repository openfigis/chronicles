package org.fao.fi.chronicles.calcuation;

import static org.junit.Assert.assertEquals;

import org.fao.fi.chronicles.util.ValueParser;
import org.junit.Test;

public class ValueParserTest {

    @Test
    public void testParseStringValueAndMultiply() {
        String stringValue = "1,558,400";
        assertEquals(1558400, ValueParser.parseStringValue(stringValue));

        stringValue = ".";
        assertEquals(0, ValueParser.parseStringValue(stringValue));
    }

}
