package org.fao.fi.chronicles.container;

/**
 * ChroniclesException is a RuntimeException
 * 
 * To avoid loads of try catches in the code, ChroniclesException is a runtime exception. This assumes however that all
 * code is very well unit tested!
 * 
 * 
 * @author Erik van Ingen
 */

public class ChroniclesException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9036045226760088331L;

    public ChroniclesException(Throwable e) {
        super(e);
    }

    public ChroniclesException(String newMessage) {
        super(newMessage);
    }

    public ChroniclesException(String msg, Throwable e) {
        super(msg, e);
    }

}
