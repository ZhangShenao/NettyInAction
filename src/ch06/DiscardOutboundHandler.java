package ch06;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * <p>Description:丢弃并释放出战消息</p>
 * @author ZhangShenao
 * @date 2017年9月13日
 */
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter{
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		//通过调用ReferenceCountUtil.release()方法类释放资源
		ReferenceCountUtil.release(msg);
		
		//通知ChannelPromise数据已经被处理了
		promise.setSuccess();
	}
}
