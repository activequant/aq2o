package com.activequant.server.trading;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.activequant.messages.AQMessages;
import com.activequant.messages.AQMessages.BaseMessage.CommandType;
import com.activequant.messages.AQMessages.Login;

public class AQServerHandler extends SimpleChannelUpstreamHandler {
	
	private String dataFolder = ".";
	private SessionManager sessionManager; 
	private MessageFactory mf = new MessageFactory();
	public AQServerHandler(SessionManager sessionManager){
		this.sessionManager = sessionManager; 
	}
	
	private static final Logger logger = Logger.getLogger(AQServerHandler.class
			.getName());

	private final AtomicLong transferredBytes = new AtomicLong();

	public long getTransferredBytes() {
		return transferredBytes.get();
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}

		// Let SimpleChannelHandler call actual event handler methods below.
		super.handleUpstream(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		AQMessages.BaseMessage bm = (AQMessages.BaseMessage) e.getMessage();		
		
		if(bm.getType().equals(CommandType.LOGIN)){
			process(ctx, e.getChannel(),  bm.getExtension(AQMessages.Login.cmd));
		}
		else{
			
		}
		
		System.out.println(bm);		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getChannel().close();
	}
	
	private void process(ChannelHandlerContext ctx, Channel channel, Login loginMessage){
		System.out.println("Processing login message.");		
		// load the user properties from our user folder. 
		try {
			Properties p = loadProperties(loginMessage.getUserId());
			// 
			if(p.getProperty("PASSWORD")!=null){
				if(p.getProperty("PASSWORD").equals(loginMessage.getPassword())){
					// ok, check if we are already logged in. 
					System.out.println("Logged in.");
					// 
					sessionManager.register(channel, loginMessage.getUserId(), loginMessage.getSessionType());
					
					// 
					channel.write(mf.buildLoginResponse("Welcome. Play fair."));
					
				}
				else{
					// wrong password.
					channel.write(mf.buildLoginResponse("User or password not known."));
					channel.close();
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			// could not login. 
			channel.write(mf.buildLoginResponse("User or password not known."));
			channel.close();
		}		
	}
	
	public Properties loadProperties(String username) throws Exception{
		String propsFile = dataFolder+File.separator + username + File.separator + "user.properties";
		Properties p = new Properties();
		p.load(new FileInputStream(propsFile));
		return p; 
		
	}
	
	
}