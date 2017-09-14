package ch06;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * <p>Description:基本的入站异常处理</p>
 * @author ZhangShenao
 * @date 2017年9月14日
 */
public class InboundExceptionHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
