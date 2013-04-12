package org.fao.fi.pivot.model.fact;

import java.io.Serializable;

/**
 * Fact contains one value of the many values of a PivotTable.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class Fact extends AbstractFact implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6330612942380780974L;
    private double value;

    public Fact(double value) {
        this.setValue(value);
    }

    public Fact() {
        this.setValue(value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new Double(this.value).toString();
    }

}
