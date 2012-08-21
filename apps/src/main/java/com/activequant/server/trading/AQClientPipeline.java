package com.activequant.server.trading;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.activequant.messages.AQMessages;
import com.google.protobuf.ExtensionRegistry;

public class AQClientPipeline implements ChannelPipelineFactory {
	public ChannelPipeline getPipeline() throws Exception {

		ExtensionRegistry registry = ExtensionRegistry.newInstance();
		AQMessages.registerAllExtensions(registry);

		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		pipeline.addLast("protobufDecoder", new ProtobufDecoder(
				AQMessages.BaseMessage.getDefaultInstance(), registry));
		// Encoder
		pipeline.addLast("frameEncoder",
				new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast("protobufEncoder", new ProtobufEncoder());
		pipeline.addLast("handler", new AQClientHandler());
		return pipeline;
	}
}
