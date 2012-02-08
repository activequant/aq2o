package com.activequant.exceptions;

/**
 * Thrown to indicate a transport exception. 
 * 
 * @author ustaudinger
 *
 */
public class InvalidDataException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public InvalidDataException(Throwable t){
    	super(t);
    }
    
    public InvalidDataException(String text){
    	super(text);
    }
    
}
