package org.fao.fi.pivot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.calculation.CalculationType;

/**
 * 
 * The column with the results of the calculation over values
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class CalculatedCollection implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8764829614462989881L;

    private CalculationType calculationType;

    private List<CalculatedField> calculatedFieldList = new ArrayList<CalculatedField>();

    public CalculatedCollection(CalculationType calculationType) {
        this.calculationType = calculationType;
    }

    public List<CalculatedField> getCalculatedFieldList() {
        return calculatedFieldList;
    }

    public void setCalculatedFieldList(List<CalculatedField> calculatedFieldList) {
        this.calculatedFieldList = calculatedFieldList;
    }

    public CalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationType calculationType) {
        this.calculationType = calculationType;
    }

}
