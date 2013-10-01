package com.activequant.component;

/**
 * Interface to communicate from a component with the server. 
 * @author ustaudinger
 *
 */
public interface IServer {

	void heartbeat(String componentId, String component);
	void statusMessage(String componentId, String statusMessage);
	void componentDescription(String componentId, String description);
	
}
