package org.fao.fi.tabular2pivot;

/**
 * TabularSeries2PivotException is a RuntimeException
 * 
 * To avoid loads of try catches in the code, TabularSeries2PivotException is a runtime exception. This assumes however
 * that all code is very well unit tested!
 * 
 * 
 * @author Erik van Ingen
 */

public class TabularSeries2PivotException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3302155407100139697L;

    public TabularSeries2PivotException(Throwable e) {
        super(e);
    }

    public TabularSeries2PivotException(String newMessage) {
        super(newMessage);
    }

    public TabularSeries2PivotException(String msg, Throwable e) {
        super(msg, e);
    }

}
