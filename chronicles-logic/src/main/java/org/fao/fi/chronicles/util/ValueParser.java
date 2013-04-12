package org.fao.fi.chronicles.util;

public final class ValueParser {

    private ValueParser() {
    }

    public static int parseStringValue(String stringValue) {
        // get rid of commas
        String stringValueWithoutCommas = stringValue.replace(",", "");
        int value = 0;
        if (!stringValue.equals(".")) {
            double doubleValue = Double.parseDouble(stringValueWithoutCommas);

            value = (int) doubleValue;
        }
        return value;
    }

}
