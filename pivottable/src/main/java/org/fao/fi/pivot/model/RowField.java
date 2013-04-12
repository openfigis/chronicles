package org.fao.fi.pivot.model;

/**
 * A row field is holding the name of the dimension used to show it in the row section.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class RowField extends StringValue {

    /**
     * 
     */
    private static final long serialVersionUID = -4359315630107462369L;

    public RowField() {
    }

    public RowField(String value) {
        this.setValue(value);
    }

}
