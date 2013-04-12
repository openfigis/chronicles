package org.fao.fi.pivot.model;

/**
 * A convenience classes for classes who do have a double value.
 * 
 * @author Erik van Ingen
 * 
 */
public abstract class DoubleValue {

    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
