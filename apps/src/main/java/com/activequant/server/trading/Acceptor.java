package com.activequant.server.trading;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * The acceptor manages all our client connections. All client connections will
 * connect through the acceptor. The acceptor keeps track of connections,
 * sessionType and accountId.
 * 
 * It translates from c2s to s2s messages (client 2 server to server to server).
 * 
 * the acceptor also takes care that client specific commands are processed.
 * 
 * We use our own native protocol over here, based on google protocol buffers.
 * 
 * 
 * @author GhostRider
 * 
 */
public class Acceptor {

	public Acceptor() {
		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new AQServerPipeline());

		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(59999));
	}

}
