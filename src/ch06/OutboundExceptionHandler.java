package ch06;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 
 * <p>Description:添加 ChannelFutureListener 到 ChannelPromise</p>
 * @author ZhangShenao
 * @date 2017年9月14日
 */
public class OutboundExceptionHandler extends ChannelOutboundHandlerAdapter{
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		promise.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (!future.isSuccess()){
					future.cause().printStackTrace();
					future.channel().close();
				}
			}
		});
	}
}
