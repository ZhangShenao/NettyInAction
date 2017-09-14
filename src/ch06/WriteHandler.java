package ch06;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * <p>Description:缓存到ChannelHandlerContext的引用</p>
 * @author ZhangShenao
 * @date 2017年9月14日
 */
public class WriteHandler extends ChannelHandlerAdapter{
	private ChannelHandlerContext ctx;		//缓存ChannelHandlerContext的引用
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		
		//存储到ChannelHandlerContext的引用以供稍后使用
		this.ctx = ctx;
	}
	
	public void send(String msg){
		//使用之前存储的ChannelHandlerContext引用来发送消息
		ctx.writeAndFlush(msg);
	}
}
