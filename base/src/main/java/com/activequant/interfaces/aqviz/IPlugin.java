package com.activequant.interfaces.aqviz;

/**
 * A plugin is a generic way to extend the visual part of ActiveQuant. 
 * It is called AFTER the main user interface has been initialized.
 *  
 * 
 * @author GhostRider
 *
 */
public interface IPlugin {
	/**
	 * Exceptions might be displayed in the audit log window. 
	 */
	void initialize() throws Exception; 
}
