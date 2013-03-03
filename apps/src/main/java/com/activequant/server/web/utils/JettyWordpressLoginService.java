package com.activequant.server.web.utils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.UserIdentity;

public class JettyWordpressLoginService extends HashLoginService{
	private List<UserIdentity> loggedInUsers = new ArrayList<UserIdentity>();

	public JettyWordpressLoginService(){
		super();
	}
	public JettyWordpressLoginService(String name, String config){
		super(name, config);
	}
	
	public JettyWordpressLoginService(String name){
		super(name);
	}

	
	@Override
	public UserIdentity login(String username, Object credentials) {
		// 
		
		// 
		System.out.println("Authenticating ...");
		if (WordpressAuthLayer.auth(username, credentials.toString())) {
			// ok, true.
			UserIdentity user = new UserIdentity() {

				@Override
				public Subject getSubject() {
					return null;
				}

				@Override
				public Principal getUserPrincipal() {
					return null;
				}

				@Override
				public boolean isUserInRole(String arg0, Scope arg1) {
					return true;
				}

			};
			if(!loggedInUsers.contains(user))
				loggedInUsers.add(user);
			return user;
		} else

			return null;
	}

	@Override
	public void logout(UserIdentity arg0) {
		// remove user from our users list.
		loggedInUsers.remove(arg0);
	}

	@Override
	public boolean validate(UserIdentity arg0) {
		if (loggedInUsers.contains(arg0))
			return true;
		return false;
	}

}
