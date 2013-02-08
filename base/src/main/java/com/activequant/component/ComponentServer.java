package com.activequant.component;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is a very simple component server.
 * 
 * @author GhostRider
 * 
 */
public class ComponentServer {
	final ApplicationContext appContext;

	public ComponentServer(String springFile) {
		appContext = new ClassPathXmlApplicationContext(
				new String[] { springFile });
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String springFile = "componentserver.xml";
		if (args.length > 0)
			springFile = args[0];
		new ComponentServer(springFile);
	}

}
