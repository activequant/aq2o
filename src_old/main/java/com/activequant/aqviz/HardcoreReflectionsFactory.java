package com.activequant.aqviz;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.reflections.Reflections;

import com.activequant.interfaces.aqviz.IInstrumentTableViz;
import com.activequant.interfaces.aqviz.IOrderTableViz;
import com.activequant.interfaces.aqviz.IQuoteTableViz;
import com.activequant.interfaces.aqviz.IVisualTable;
import com.activequant.interfaces.trading.IExchange;
import com.activequant.trading.datamodel.AQTableDataBase;

public class HardcoreReflectionsFactory {

	Set<Class<? extends IVisualTable>>  subTypes;
	/**
	 * @param args
	 */
	public HardcoreReflectionsFactory() {
		this("com.activequant.datamodel.viz");
	}

	
	public HardcoreReflectionsFactory(String firstSearchPackage){
		Reflections reflections = new Reflections(firstSearchPackage);
		subTypes = reflections.getSubTypesOf(IVisualTable.class);
		System.out.println(subTypes);

		if (subTypes.isEmpty()) {
			// scan for the default viz implementation - the doing-nothing
			// implementation.
			reflections = new Reflections("com.activequant.aqviz.noviz");
			subTypes = reflections.getSubTypesOf(IVisualTable.class);
		}
	}
	public IVisualTable getVisualTableViz(String s, AQTableDataBase aqt) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		for(Class c : subTypes){
			if(c.getSimpleName().equals("VisualTable")){
				Constructor constr = c.getConstructor(String.class, AQTableDataBase.class);
				return (IVisualTable)constr.newInstance(s, aqt);
			}
		}
		throw new RuntimeException("Could not instantiate visual table. This installation is totally screwed up.");
		
	}
	
	public IQuoteTableViz getQuoteTableViz(String s, AQTableDataBase aqt, IExchange exch) 
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		for(Class c : subTypes){
			if(c.getSimpleName().equals("QuoteTableViz")){
				Constructor constr = c.getConstructor(String.class, AQTableDataBase.class, IExchange.class);
				return (IQuoteTableViz)constr.newInstance(s, aqt, exch);
			}
		}
		throw new RuntimeException("Could not instantiate quote table. This installation is totally screwed up.");
		
	}

	public IOrderTableViz getOrderTableViz(String s, AQTableDataBase aqt, IExchange exch) 
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		for(Class c : subTypes){
			if(c.getSimpleName().equals("OrderTableViz")){
				Constructor constr = c.getConstructor(String.class, AQTableDataBase.class, IExchange.class);
				return (IOrderTableViz)constr.newInstance(s, aqt, exch);
			}
		}
		throw new RuntimeException("Could not instantiate order table. This installation is totally screwed up.");
		
	}

	public IInstrumentTableViz getInstrumentTableViz(String s, AQTableDataBase aqt, IExchange exch) 
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		for(Class c : subTypes){
			if(c.getSimpleName().equals("InstrumentTableViz")){
				Constructor constr = c.getConstructor(String.class, AQTableDataBase.class, IExchange.class);
				return (IInstrumentTableViz)constr.newInstance(s, aqt, exch);
			}
		}
		throw new RuntimeException("Could not instantiate instrument table. This installation is totally screwed up.");
		
	}

	
	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		new HardcoreReflectionsFactory().getVisualTableViz("Title", null);
	}

}
