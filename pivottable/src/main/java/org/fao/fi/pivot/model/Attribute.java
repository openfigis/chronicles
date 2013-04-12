package org.fao.fi.pivot.model;

import java.io.Serializable;

/**
 * An attribute as in SDMX terms. An entry of a series consists of a key, value and attributes.
 * 
 * @author Erik van Ingen
 * 
 */
public class Attribute implements Serializable {

    private int value;

    /**
     * 
     */
    private static final long serialVersionUID = 1638308165444050987L;

    public Attribute(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
