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

	// local pointer to the appcontext. 
	// Dunno why i keep it at the moment, but
	// we never know.
	final ApplicationContext appContext;

	/**
	 * 
	 * @param springFile
	 */
	public ComponentServer(String springFile) {
		appContext = new ClassPathXmlApplicationContext(
				new String[] { springFile });
	}

	/**
	 * Takes a spring bean definition file as an argument.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String springFile = "componentserver.xml";
		if (args.length > 0)
			springFile = args[0];
		new ComponentServer(springFile);
	}

}
