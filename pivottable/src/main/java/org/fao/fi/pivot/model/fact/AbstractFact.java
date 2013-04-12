package org.fao.fi.pivot.model.fact;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.model.Attribute;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.Row;

/**
 * Fact contains one value of the many values of a PivotTable.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public abstract class AbstractFact {

    private Row row;
    private ColumnField columnField;
    private List<Attribute> attributeList = new ArrayList<Attribute>();

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public ColumnField getColumnField() {
        return columnField;
    }

    public void setColumnField(ColumnField columnField) {
        this.columnField = columnField;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

}
