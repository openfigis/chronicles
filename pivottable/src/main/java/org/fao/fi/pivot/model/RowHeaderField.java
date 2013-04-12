package org.fao.fi.pivot.model;

import java.io.Serializable;

/**
 * 
 * The RowHeaderField has the concept name used for a particular dimension. It is shown in the rowsection.
 * 
 * @author Erik van Ingen
 * 
 */
public class RowHeaderField extends ConceptName implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -9184428408337606054L;

    public RowHeaderField() {

    }

    public RowHeaderField(String conceptName) {
        this.setConceptName(conceptName);
    }

}
