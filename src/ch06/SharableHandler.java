package ch06;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * <p>Description:可共享的ChannelHandler</p>
 * <p>一个ChannelHandler可能从属于多个ChannelPipeline,这时需要使用@Sharable注解标注,并且要保证其线程安全性</p>
 * @author ZhangShenao
 * @date 2017年9月14日
 */
@Sharable
public class SharableHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("Channel Read Msg: " + msg);
		
		//转发给下一个ChannelHandler
		ctx.fireChannelRead(msg);
	}
}
