package ch02;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 
 * <p>Description:处理Echo客户端的业务逻辑</p>
 * @author ZhangShenao
 * @date 2017年7月11日
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//当被通知Channel是活跃的时候,向服务器发送一条信息
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", Charset.forName("UTF-8")));
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
			throws Exception {
		//记录已接收消息的转储,并打印到控制台
		System.out.println(
                "Client received: " + msg.toString(Charset.forName("UTF-8")));
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//打印异常堆栈信息
		cause.printStackTrace();
		
		//关闭ChannelHandlerContext
		ctx.close();
	}
}
