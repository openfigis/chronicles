package org.fao.fi.pivot.model;

import java.io.Serializable;

/**
 * A convenience classes for classes who do have a String value.
 * 
 * 
 * @author Erik van Ingen
 * 
 */

public abstract class StringValue implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5212420867692281058L;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
