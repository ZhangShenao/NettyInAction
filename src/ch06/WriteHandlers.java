package ch06;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * 
 * <p>Description:使用ChannelHandlerContext</p>
 * @author ZhangShenao
 * @date 2017年9月14日
 */
public class WriteHandlers extends ChannelInboundHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		writeViaChannel(ctx);
		writeViaChannelPipeline(ctx);
	}
	
	/**
	 * 从ChannelHandlerContext访问Channel
	 */
	public void writeViaChannel(ChannelHandlerContext ctx){
		//获取与ChannelHandlerContext相关联的Channel的引用
		Channel channel = ctx.channel();
		
		//通过Channel写入缓冲区
		channel.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));
	}
	
	/**
	 * 通过ChannelHandlerContext访问ChannelPipeline
	 */
	public void writeViaChannelPipeline(ChannelHandlerContext ctx){
		//获取与ChannelHandlerContext相关联的ChannelPipeline的引用
		ChannelPipeline pipeline = ctx.pipeline();
		
		//通过ChannelPipeline写入缓冲区
		pipeline.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));
	}
	
	/**
	 * 调用ChannelHandlerContext的write方法
	 * @param ctx
	 */
	public void writeViaChannelHandlerContext(ChannelHandlerContext ctx){
		//write()方法将把缓冲区中的数据发送到下一个ChannelHandler
		ctx.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));
	}
}
