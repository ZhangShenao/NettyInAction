package ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * 
 * <p>Description:使用protobuf</p>
 * @author ZhangShenao
 * @date 2017年10月22日
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		//添加ProtobufVarint32FrameDecoder以分隔帧
		pipeline.addLast(new ProtobufVarint32FrameDecoder());
		
		//添加ProtobufEncoder以处理消息的编码
//		pipeline.addLast(new ProtobufEncoder());
		
		//添加ProtobufEncoder以处理消息的解码
//		pipeline.addLast(new ProtobufDecoder());
	}

}
