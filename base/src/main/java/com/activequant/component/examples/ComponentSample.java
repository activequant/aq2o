package com.activequant.component.examples;

import com.activequant.component.ComponentBase;
import com.activequant.interfaces.transport.ITransportFactory;

public class ComponentSample extends ComponentBase {
	/**
	 * Use this constructor to have Spring's autowiring kick in ..
	 * 
	 * @param transFac
	 * @throws Exception
	 */
	public ComponentSample(ITransportFactory transFac) throws Exception {
		super("ComponentSample", transFac);
	}

	@Override
	public String getDescription() {
		return "This is our very basic component example.";
	}

}
