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
			// create a script engine manager
			// ScriptEngineManager factory = new ScriptEngineManager();
			// create an R engine
			FileSystemManager fileSystemManager = VFS.getManager();
			//FileObject jarFile = fileSystemManager.resolveFile( "jar:file:///home/ustaudinger/.m2/repository/org/renjin/renjin-core/0.6.4-SNAPSHOT/renjin-core-0.6.4-SNAPSHOT.jar");
			//fileSystemManager.getFilesCache().putFile(jarFile);
			
			FileObject workingDirectory = fileSystemManager.getBaseFile();
			Context context = Context.newTopLevelContext(fileSystemManager, "./", workingDirectory);
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
