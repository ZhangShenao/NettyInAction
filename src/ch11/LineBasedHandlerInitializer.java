package ch11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * 
 * <p>Description:处理由行尾符分隔的帧</p>
 * @author ZhangShenao
 * @date 2017年10月18日
 */
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		//LineBasedFrameDecoder将提取的帧转发给下一个ChannelInboundHandler
		pipeline.addLast(new LineBasedFrameDecoder(64 * 1024));
		
		//添加FrameHandler以接收分隔后的单个帧
		pipeline.addLast(new FrameHandler());
	}
	
	public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
				throws Exception {
			//处理传入的单个帧的内容
		}
		
	}

}
