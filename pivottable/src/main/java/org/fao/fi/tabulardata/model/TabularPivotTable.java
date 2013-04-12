package org.fao.fi.tabulardata.model;

/**
 * A specific type of tabular data is when it is representing a pivot table. The name of the fact are seen in the first
 * row.
 * 
 * The TabularPivotTable has 2 rows at the top. The first one with 2 fields, the factConceptName and the conceptname of
 * the horizontal values. The second row has the dimension names of the rowsection and the values of the columnsection.
 * 
 * 
 * Can be used to represent a csv file, containing a pivot table.
 * 
 * See for terminology this page: http://km.fao.org/FIGISwiki/index.php/PivotTable
 * 
 * 
 * @author Erik van Ingen
 * 
 */

public class TabularPivotTable extends SimpleTabularPivotTable {

}
