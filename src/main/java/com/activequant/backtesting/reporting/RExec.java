package com.activequant.backtesting.reporting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RExec {

	public RExec(String script, String[] args) throws IOException, InterruptedException {
		String s = null;

		String cmdArgs = ""; 
		for(String a : args)
			cmdArgs += a + " "; 
		
		String[] cmd = { "/bin/sh", "-c", "R --no-save --no-restore --args "+cmdArgs+ " < " + script };

		Process p = Runtime.getRuntime().exec(cmd);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		// read the output from the command
		System.out.println("Here is the standard output of the command:\n");
		while ((s = stdInput.readLine()) != null) {
			System.out.println(s);
		}

		// read any errors from the attempted command
		System.out.println("Here is the standard error of the command (if any):\n");
		while ((s = stdError.readLine()) != null) {
			System.out.println(s);
		}	
		p.waitFor();
	}

}
