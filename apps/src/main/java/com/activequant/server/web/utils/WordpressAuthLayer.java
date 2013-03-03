package com.activequant.server.web.utils;

import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * Checks user credentials against a wordpress authentication database. Does a
 * plain SQL check. So, this means it has to have SQL read access to the
 * wordpress DB.
 * 
 * @author GhostRider
 * 
 */
public class WordpressAuthLayer {

	// 
	public static boolean auth(String username, String password) {
		try {
			// 
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL("http://aqexcel.activequant.com/xmlrpc.php"));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
			Object[] params = new Object[] { username, password };
			Boolean result = (Boolean) client.execute("wpse39662.login", params);
			// 
			return result; 
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 
	public static void main(String[] args) {
		System.out.println(WordpressAuthLayer.auth("admin", ""));

	}
}
