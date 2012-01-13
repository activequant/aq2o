package com.activequant.exceptions;

/**
 * Thrown to indicate a transport exception. 
 * 
 * @author ustaudinger
 *
 */
public class IncompleteOrderInstructions extends Exception {
    private static final long serialVersionUID = 1L;
    
    public IncompleteOrderInstructions(Throwable t){
    	super(t);
    }
    
    public IncompleteOrderInstructions(String text){
    	super(text);
    }
    
}
