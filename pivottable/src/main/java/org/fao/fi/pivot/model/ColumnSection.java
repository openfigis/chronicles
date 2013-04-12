package org.fao.fi.pivot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A pivot table consists of a column section and a row section. The columns section refers to the horizontal top level
 * part of the pivot table.
 * 
 * @author Erik van Ingen
 * 
 */
public class ColumnSection extends ConceptName implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4335032374283768292L;
    private List<ColumnField> columnFieldList = new ArrayList<ColumnField>();
    private List<CalculatedCollection> aggregatedColumnList = new ArrayList<CalculatedCollection>();

    public List<ColumnField> getColumnFieldList() {
        return columnFieldList;
    }

    public void setColumnFieldList(List<ColumnField> columnFieldList) {
        this.columnFieldList = columnFieldList;
    }

    public List<CalculatedCollection> getCalculatedCollectionList() {
        return aggregatedColumnList;
    }

    public void setCalculatedCollectionList(List<CalculatedCollection> aggregatedColumnList) {
        this.aggregatedColumnList = aggregatedColumnList;
    }

}
