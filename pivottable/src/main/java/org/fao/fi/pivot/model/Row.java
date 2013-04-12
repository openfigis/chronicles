package org.fao.fi.pivot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.model.fact.AbstractFact;

/**
 * A row part of the row section containing at least one dimension, and the facts for each column dimension.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class Row implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3290915559860671041L;
    private List<AbstractFact> factList = new ArrayList<AbstractFact>();
    private List<RowField> rowFieldList = new ArrayList<RowField>();

    public List<AbstractFact> getFactList() {
        return factList;
    }

    public void setFactList(List<AbstractFact> factList) {
        this.factList = factList;
    }

    public List<RowField> getRowFieldList() {
        return rowFieldList;
    }

    public void setRowFieldList(List<RowField> rowFieldList) {
        this.rowFieldList = rowFieldList;
    }

}
