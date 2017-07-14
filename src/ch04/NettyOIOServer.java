package ch04;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 
 * <p>Description:使用Netty实现的阻塞式网络编程</p>
 * @author ZhangShenao
 * @date 2017年7月14日
 */
public class NettyOIOServer {
	private final ByteBuf msg =
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hello Client!\r\n", Charset.forName("UTF-8")));
	
	public void serve(int port){
		 EventLoopGroup group = null;
		 
		try {
			//创建阻塞式EventLoopGroup
			group = new OioEventLoopGroup();
			
			//创建服务器的引导ServerBootstrap
			ServerBootstrap b = new ServerBootstrap();
			
			b.group(group)
			
			//指定Channel类型为OioServerSocketChannel
			.channel(OioServerSocketChannel.class)
			
			//指定服务器的本地地址
			.localAddress(new InetSocketAddress(port))
			
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					 //添加一个 ChannelInboundHandlerAdapter以拦截和处理事件
					ch.pipeline().addLast(new ServerChannelHandler());
				}
			});
			
			//绑定到指定端口,阻塞直到操作完成
			ChannelFuture f = b.bind().sync();
			
			//阻塞直到Channel关闭
			f.channel().closeFuture().sync();
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
	}
	
	/**
	 * 
	 * <p>Description:服务器的Channel逻辑处理类</p>
	 * @author ZhangShenao
	 * @date 2017年7月14日
	 */
	private class ServerChannelHandler extends ChannelInboundHandlerAdapter{
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			//Channel通道建立时,向客户端发送数据
			ctx.writeAndFlush(msg.duplicate()).
			//添加ChannelFutureListener,消息一写完就关闭连接
			addListener(ChannelFutureListener.CLOSE);
		}
	}
}
