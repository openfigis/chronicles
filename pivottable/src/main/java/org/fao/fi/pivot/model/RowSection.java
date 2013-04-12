package org.fao.fi.pivot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The row section holds the names of the dimensions on the left side of a PivotTable and the references to the rows.
 * 
 * @author Erik van Ingen
 * 
 */

public class RowSection implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8283560800210111261L;
    private List<Row> rowList = new ArrayList<Row>();
    private List<RowHeaderField> rowHeaderFieldList = new ArrayList<RowHeaderField>();
    private List<CalculatedCollection> aggregatedRowList = new ArrayList<CalculatedCollection>();

    public List<Row> getRowList() {
        return rowList;
    }

    public void setRowList(List<Row> rowList) {
        this.rowList = rowList;
    }

    public List<RowHeaderField> getRowHeaderFieldList() {
        return rowHeaderFieldList;
    }

    public void setRowHeaderFieldList(List<RowHeaderField> rowHeaderFieldList) {
        this.rowHeaderFieldList = rowHeaderFieldList;
    }

    public List<CalculatedCollection> getCalculatedCollectionList() {
        return aggregatedRowList;
    }

    public void setCalculatedCollectionList(List<CalculatedCollection> aggregatedRowList) {
        this.aggregatedRowList = aggregatedRowList;
    }

}
