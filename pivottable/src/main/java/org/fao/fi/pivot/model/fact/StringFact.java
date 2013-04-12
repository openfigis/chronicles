package org.fao.fi.pivot.model.fact;

import java.io.Serializable;

public class StringFact extends AbstractFact implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7484046194539621727L;
    private String value;

    public StringFact(String value) {
        this.setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
