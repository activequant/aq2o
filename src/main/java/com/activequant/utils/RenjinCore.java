package com.activequant.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.renjin.eval.Context;
import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.DoubleVector;

public class RenjinCore {

	private ScriptEngine engine;

	public RenjinCore() {
		try {			
			Context context = Context.newTopLevelContext();
			context.getGlobals().setLibraryPaths("");
			engine = new RenjinScriptEngineFactory().getScriptEngine(context);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void execute(String cmd) throws ScriptException {
		engine.eval(cmd);
	}

	public void put(String key, Object object) {
		engine.put(key, object);
	}

	public Object get(String key) {
		return engine.get(key);
	}

	public DoubleVector getDoubleVector(String key) {
		return (DoubleVector) engine.get(key);
	}
	
	/**
	 * Returns a single value as double.
	 * @param key
	 * @return
	 */
	public Double getDouble(String key){
		return ((DoubleVector) engine.get(key)).get(0);	
	}
	
	

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
