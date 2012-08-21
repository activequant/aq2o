package com.activequant.server.trading;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.activequant.messages.AQMessages;

public class AQClientHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(AQClientHandler.class
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
		System.out.println(bm.toString());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getChannel().close();
	}
}