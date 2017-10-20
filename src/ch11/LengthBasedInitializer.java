package ch11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 
 * <p>Description:使用LengthFieldBasedFrameDecoder解码器处理基于长度的协议</p>
 * <p>将帧长度编码到帧起始的前8个字节中的消息</p>
 * @author ZhangShenao
 * @date 2017年10月20日
 */
public class LengthBasedInitializer extends ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast(new LengthFieldBasedFrameDecoder(62 * 1024, 0, 8));
	}
	
	public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
				throws Exception {
			//处理帧数据
		}
		
	}

}
