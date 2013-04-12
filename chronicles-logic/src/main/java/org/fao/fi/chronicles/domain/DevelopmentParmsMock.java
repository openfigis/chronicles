package org.fao.fi.chronicles.domain;

public class DevelopmentParmsMock {

    public static final DevelopmentParms mockIt() {
        DevelopmentParms p = new DevelopmentParms();
        p.setClusters(10);
        p.setEndYear(1980);
        p.setStartYear(1971);
        p.setTop(50);
        String a[] = { "508", "558", "364" };
        p.setUn(a);
        return p;
    }

}
