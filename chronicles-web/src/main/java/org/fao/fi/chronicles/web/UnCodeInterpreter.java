package org.fao.fi.chronicles.web;

public class UnCodeInterpreter {

    private static int DIGITS = 3;

    /**
     * split the value into portions of 3 digits.
     * 
     * @param un
     * @return
     */
    static String[] parse(String un) {
        int n = un.length() / DIGITS;
        String array[] = new String[n];
        for (int i = 0; i < n; i++) {
            int start = i * DIGITS;
            int stop = start + DIGITS;
            array[i] = un.substring(start, stop);
        }
        return array;
    }
}
