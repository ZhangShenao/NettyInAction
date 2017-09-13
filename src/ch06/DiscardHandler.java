package ch06;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * <p>Description:释放消息资源</p>
 * @author ZhangShenao
 * @date 2017年9月13日
 */
public class DiscardHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//丢弃已接收到的消息
		ReferenceCountUtil.release(msg);
	}
}
