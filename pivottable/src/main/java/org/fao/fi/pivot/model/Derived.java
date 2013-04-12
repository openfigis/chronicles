package org.fao.fi.pivot.model;

import java.io.Serializable;

/**
 * A derived row is a specific type of row. It is a row which is part of the rowsection. The derived row is being
 * calculated based on values of other rows. The derived row is often used to replace a certain set of rows.
 * 
 * For example there is a set of 900 rows. The top 100 rows are selected and the other 800 are aggregated to 1 (derived)
 * row.
 * 
 * 
 * @author Erik van Ingen
 * 
 */

public class Derived extends Row implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1303262901513104294L;

}
