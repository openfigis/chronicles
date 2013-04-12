package org.fao.fi.pivot;

/**
 * PivotTableException is a RuntimeException
 * 
 * To avoid loads of try catches in the code, PivotTableException is a runtime exception. This assumes however that all
 * code is very well unit tested!
 * 
 * 
 * @author Erik van Ingen
 */

public class PivotTableException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9036045226760088331L;

    public PivotTableException(Throwable e) {
        super(e);
    }

    public PivotTableException(String newMessage) {
        super(newMessage);
    }

    public PivotTableException(String msg, Throwable e) {
        super(msg, e);
    }

}
