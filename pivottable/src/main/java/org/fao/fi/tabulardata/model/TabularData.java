package org.fao.fi.tabulardata.model;

/**
 * The most basic representation of tabular data.
 * 
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class TabularData {

    private String[][] table;

    public String[][] getTable() {
        return table;
    }

    public void setTable(String[][] table) {
        this.table = table.clone();
    }

}
