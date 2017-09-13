package ch06;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 
 * <p>Description:SimpleChannelInboundHandler会自动释放资源</p>
 * @author ZhangShenao
 * @date 2017年9月13日
 */
public class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//不需要任何的显式资源释放
	}

}
