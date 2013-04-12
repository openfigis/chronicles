package org.fao.fi.chronicles.domain;

/**
 * The parameters used to generate the development chart.
 * 
 * @author Erik van Ingen
 * 
 */
public class DevelopmentParms extends CatchParms {

    private int clusters;

    public int getClusters() {
        return clusters;
    }

    public void setClusters(int clusters) {
        this.clusters = clusters;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + clusters;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DevelopmentParms other = (DevelopmentParms) obj;
        if (clusters != other.clusters)
            return false;
        return true;
    }

}
