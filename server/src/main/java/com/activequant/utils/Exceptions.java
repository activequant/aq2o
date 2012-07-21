package com.activequant.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Exceptions {
	/**
	 * from http://javahowto.blogspot.com/2006/08/save-exception-stacktrace-to-string.html 
	 * @param e
	 * @return
	 */
	public static String stackTraceToString(Throwable e) {
		String retValue = null;
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			retValue = sw.toString();
		} finally {
			try {
				if (pw != null)
					pw.close();
				if (sw != null)
					sw.close();
			} catch (IOException ignore) {
			}
		}
		return retValue;
	}
}
