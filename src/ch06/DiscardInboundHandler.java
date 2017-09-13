package ch06;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * <p>Description:消费并释放入站消息</p>
 * @author ZhangShenao
 * @date 2017年9月13日
 */
public class DiscardInboundHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//通过调用ReferenceCountUtil.release()方法类释放资源
		ReferenceCountUtil.release(msg);
	}
}
