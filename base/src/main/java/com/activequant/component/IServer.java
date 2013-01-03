package com.activequant.component;

public interface IServer {

	void heartbeat(String componentId, String component);
	void statusMessage(String componentId, String statusMessage);
	void componentDescription(String componentId, String description);
	
}
