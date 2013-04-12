package org.fao.fi.pivot.model;

import java.io.Serializable;

/**
 * A convenience class for classes who do have a concept name.
 * 
 * @author Erik van Ingen
 * 
 */
public abstract class ConceptName implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -300004431488128611L;
    private String conceptName;

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

}
