package com.activequant.server.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * 
 * @author GhostRider
 * 
 */
public class RefDumpServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final DataSource ds; 	

	public RefDumpServlet(DataSource ds) {
		this.ds = ds; 
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<?, ?> paramMap = req.getParameterMap();
		PrintWriter response = resp.getWriter();
		try {

			if (paramMap.containsKey("TABLESTATE")) {
				String table =((String[])paramMap.get("TABLESTATE"))[0];
				String created =((String[])paramMap.get("CREATED"))[0];
				//
				// this works only with the mybatis implementation. Sorry for taking this shortcut.  
				// 
				String query = "SELECT distinct(keyVal) from "+table+" where created>"+created;
//				
				Statement ps = ds.getConnection().createStatement();				
				ResultSet rs = ps.executeQuery(query);
				while(rs.next()){
					String keyVal = rs.getString(1);
					response.print(keyVal);
					
					response.print("\n");
					response.flush();
				}
				ps.close();				
			} 
			else if (paramMap.containsKey("TABLEDUMP")) {
				String table =((String[])paramMap.get("TABLEDUMP"))[0];
				String createdIn =((String[])paramMap.get("CREATED"))[0];
				//
				// this works only with the mybatis implementation. Sorry for taking this shortcut.  
				// 
				String query = "SELECT keyVal, created, fieldName, stringVal, longVal, doubleVal from "+table+" where created > " + createdIn;
				//				
				Statement ps = ds.getConnection().createStatement();				
				ResultSet rs = ps.executeQuery(query);
				while(rs.next()){
					String keyVal = rs.getString(1);
					Long created = rs.getLong(2);
					String fieldName = rs.getString(3);
					String stringVal = rs.getString(4);
					if(rs.wasNull())stringVal = null; 
					Long longVal = rs.getLong(5);
					if(rs.wasNull())longVal = null; 
					Double doubleVal = rs.getDouble(6);
					if(rs.wasNull())doubleVal = null; 
					
					response.print(keyVal);
					response.print("-;-");
					response.print(created);
					response.print("-;-");
					response.print(fieldName);
					response.print("-;-");
					if(stringVal!=null)response.print(stringVal);
					response.print("-;-");
					if(longVal!=null)response.print(longVal);
					response.print("-;-");
					if(doubleVal!=null)response.print(doubleVal);					
					response.print("-;-");
					response.print("\n");
					response.flush();
				}
				ps.close();
				
			}						
			else {
				response.print("Unauthorized.");
				response.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			response.print(ex);
			response.flush();
		}
	}
}