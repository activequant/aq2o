package com.activequant.utils.mail;

public class HtmlMail {
	
	private StringBuffer body; 
	private String subject;
	
	public StringBuffer getBody() {
		return body;
	}
	public void setBody(StringBuffer body) {
		this.body = body;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void append(String text){
		body.append(text);
	}
	
	public void newLine(){
		body.append("<br>\n");
	}
	
}
