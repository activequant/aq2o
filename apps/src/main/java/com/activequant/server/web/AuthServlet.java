package com.activequant.server.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.server.components.SessionTrackerComponent;
import com.activequant.server.web.utils.WordpressAuthLayer;


/**
 * 
 * Protocol: http://localhost:44444/flatauth/?UID=admin&PWD=XXXX&IP=12
 * returns 0 (unauthorized), 1 (authorized) or a string message with an IP where a user is already logged in. 
 * @author GhostRider
 *
 */
public class AuthServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IArchiveFactory archFac;
	private Logger log = Logger.getLogger(AuthServlet.class);

	public AuthServlet() {
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		String retCode = "0"; 
		@SuppressWarnings("rawtypes")
		Map paramMap = req.getParameterMap();
		if (paramMap.containsKey("UID") && paramMap.containsKey("PWD") && paramMap.containsKey("IP")) {
			String uid = ((String[])paramMap.get("UID"))[0];
			String pwd = ((String[])paramMap.get("PWD"))[0];
			String ip = ((String[]) paramMap.get("IP"))[0];
			// now let's authenticate against our central database. 
			// 
			log.info("Authenticating " + uid + " at " + ip);
			if(WordpressAuthLayer.auth(uid, pwd)){
				// ok. check if we already have an active session. 
				if(SessionTrackerComponent.hasUserSession(uid, ip)!=null)
					retCode = SessionTrackerComponent.hasUserSession(uid, ip);
				else{
					// track that login.
					SessionTrackerComponent.trackUserSession(uid, ip);
					retCode = "1"; 
					// 
				}
				// 
			}
			else{
				log.info("Not authorized.");
				retCode = "0"; 
			}			
			
		} else {
			retCode = "0"; 
		}
		
		response.getWriter().print(""+retCode);
		response.getWriter().flush();
	}

}