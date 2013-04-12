package org.fao.fi.pivot.jfreechart;

public class ChartTextMock {

    private ChartTextMock() {
        // Utility classes should not have a public or default constructor
    }

    public static ChartText mockIt() {
        ChartText c = new ChartText();
        c.setTitle("Obama backs India's UN seat bid");
        c.setxAxisTitle("Yemen cleric in US death message");
        c.setyAxisTitle("Iraqi leaders hold government power-sharing talks");
        return c;
    }

}
