package com.activequant.server.trading;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.activequant.messages.AQMessages.BaseMessage;

public class AcceptorClientExample {

	static <Type> BaseMessage wrap(BaseMessage.CommandType type, BaseMessage.GeneratedExtension<BaseMessage, Type> extension, Type cmd) {
	    return BaseMessage.newBuilder().setType(type).setExtension(extension, cmd).build();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new AQClientPipeline());

		ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress(
				"localhost", 59999));
		Channel channel = connectFuture.awaitUninterruptibly().getChannel();
		
		
		MessageFactory mf = new MessageFactory();
		BaseMessage bm = mf.buildLogin("ulrich", "aaaa", "PRICE");
		
		//System.out.println(l.toString());
		channel.write(bm);

	}

}
