package com.activequant.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.renjin.eval.Context;
import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.DoubleVector;
/**
 * 
 * @author GhostRider
 *
 */
public class RenjinCore {

	private ScriptEngine engine;

	/**
	 * Initialized the renjin engine. 
	 */
	public RenjinCore() {
		try {			
			Context context = Context.newTopLevelContext();
			context.getGlobals().setLibraryPaths("");		
			context.init();
			engine = new RenjinScriptEngineFactory().getScriptEngine(context);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Executes an R command in the engine environment. 
	 * @param cmd
	 * @throws ScriptException
	 */
	public void execute(String cmd) throws ScriptException {
		engine.eval(cmd);
	}

	/**
	 * Puts an object into the engine's environment. 
	 * @param key
	 * @param object
	 */
	public void put(String key, Object object) {
		engine.put(key, object);
	}

	
	/**
	 * Fetches an object from the engine's environment. 
	 * @param key
	 * @return a renjin object, for example Double Vector
	 */
	public Object get(String key) {
		return engine.get(key);
	}

	/**
	 * convenience method. fetches an object from engine env and casts it to double vector.  
	 * 
	 * @param key
	 * @return
	 */
	public DoubleVector getDoubleVector(String key) {
		return (DoubleVector) engine.get(key);
	}
	
	/**
	 * Returns a single value as double. Can break. 
	 * 
	 * @param key
	 * @return
	 */
	public Double getDouble(String key){
		return ((DoubleVector) engine.get(key)).get(0);	
	}
	
	

	/**
	 * Just some basic fun testing method. 
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RenjinCore r = new RenjinCore();
		r.put("a", 1);
		r.execute("b = c(a, sqrt(a + 1));");
		r.execute("b = c(a, sqrt(a + 1), sqrt(50));");
		r.execute("d = sum(b);");
		// r.execute("sdb = sd(b);");
		//r.execute("plot(b);");
		System.out.println("Obj: " + r.get("b") + " " + r.get("b").getClass());
		System.out.println(r.get("d"));
	}

}
