package com.activequant.server.trading;

public class MessageServer {

	public MessageServer(){
		// 
		new Acceptor();
		new BigPipe();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MessageServer();		
	}

}
