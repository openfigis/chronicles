package org.fao.fi.pivot.model;

import java.io.Serializable;

/**
 * This class is the entry point of the model for the PivotTable. The Wikipedia page has been used to derive the
 * vocabulary. Also SDMX is used for vocabulary like attribute, dimension, concept and value.
 * 
 * 
 * http://en.wikipedia.org/wiki/PivotTable
 * 
 * http://km.fao.org/FIGISwiki/index.php/PivotTable
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class PivotTable implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5480126911286734481L;

    private ColumnSection columnSection = new ColumnSection();
    private RowSection rowSection = new RowSection();
    private String factConceptName;

    public ColumnSection getColumnSection() {
        return columnSection;
    }

    public void setColumnSection(ColumnSection columnSection) {
        this.columnSection = columnSection;
    }

    public RowSection getRowSection() {
        return rowSection;
    }

    public void setRowSection(RowSection rowSection) {
        this.rowSection = rowSection;
    }

    public String getFactConceptName() {
        return factConceptName;
    }

    public void setFactConceptName(String factConceptName) {
        this.factConceptName = factConceptName;
    }

}
