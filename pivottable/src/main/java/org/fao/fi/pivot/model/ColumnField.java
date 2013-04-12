package org.fao.fi.pivot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.model.fact.AbstractFact;

/**
 * A column field appears on the horizontal top level area of a PivotTable. It is the dimension chosen to appear on
 * horizontal level.
 * 
 * @author Erik van Ingen
 * 
 */
public class ColumnField extends StringValue implements Serializable {

    private List<Attribute> attributeList = new ArrayList<Attribute>();
    private List<AbstractFact> factList = new ArrayList<AbstractFact>();

    /**
     * 
     */
    private static final long serialVersionUID = -3341886511753872648L;

    public ColumnField() {
    }

    public ColumnField(String value) {
        this.setValue(value);
    }

    public List<AbstractFact> getFactList() {
        return factList;
    }

    public void setFactList(List<AbstractFact> factList) {
        this.factList = factList;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
