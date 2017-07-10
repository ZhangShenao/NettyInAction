package ch02;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;

/**
 * 
 * <p>Description:该组件实现了服务器从对客户端接收的数据的处理,即它的业务逻辑。</p>
 * @author ZhangShenao
 * @date 2017年7月10日
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//获取客户端输入
		ByteBuf in = (ByteBuf)msg;
		
		 //将消息记录到控制台
        System.out.println(
                "Server received: " + in.toString(Charset.forName("UTF-8")));
        
        //将接收到的消息写给发送者，而不冲刷出站消息
        ctx.write(in);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//将未决消息冲刷到远程节点，并且关闭该 Channel
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//打印异常栈跟踪
        cause.printStackTrace();
        
        //关闭该Channel
        ctx.close();
	}
}
