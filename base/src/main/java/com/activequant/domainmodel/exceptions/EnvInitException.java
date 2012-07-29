package com.activequant.domainmodel.exceptions;

/**
 * Thrown to indicate a transport exception. 
 * 
 * @author ustaudinger
 *
 */
public class EnvInitException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public EnvInitException(Throwable t){
    	super(t);
    }
    
    public EnvInitException(String text){
    	super(text);
    }
    
}
