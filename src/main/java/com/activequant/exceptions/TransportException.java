package com.activequant.exceptions;

/**
 * Thrown to indicate a transport exception. 
 * 
 * @author ustaudinger
 *
 */
public class TransportException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public TransportException(Throwable t){
    	super(t);
    }
}
