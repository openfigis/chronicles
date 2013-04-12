package org.fao.fi.chronicles.domain;

import java.util.Arrays;

/**
 * The parameters used to generate the catch chart.
 * 
 * @author Erik van Ingen
 * 
 */
public class CatchParms {

    private int startYear;
    private int endYear;
    private int top;
    private String[] un;

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

   
    public String[] getUn() {
        return un;
    }

    public void setUn(String[] un) {
        this.un = un;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + endYear;
        result = prime * result + startYear;
        result = prime * result + top;
        result = prime * result + Arrays.hashCode(un);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CatchParms other = (CatchParms) obj;
        if (endYear != other.endYear)
            return false;
        if (startYear != other.startYear)
            return false;
        if (top != other.top)
            return false;
        if (!Arrays.equals(un, other.un))
            return false;
        return true;
    }

 

}
