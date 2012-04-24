package com.activequant.utils;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class RenjinCore {

	private ScriptEngine engine;

	public RenjinCore() {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		// create an R engine
		engine = factory.getEngineByName("Renjin");

	}

	public void execute(String cmd) throws ScriptException {
		engine.eval(cmd);
	}
	
	public void put(String key, Object object){
		engine.put(key, object);
	}

	public Object get(String key){
		return engine.get(key);
	}
	
	public static void main(String[] args) throws ScriptException {
		RenjinCore r = new RenjinCore();
		r.put("a", 1);		
		r.execute("b = c(a, sqrt(a + 1));");
		r.execute("b = c(a, sqrt(a + 1), sqrt(50));");
		r.execute("sdb = sd(b);");
		System.out.println("Obj: " + r.get("b") + " " + r.get("b").getClass());
		System.out.println(r.get("sdb"));
	}

}
