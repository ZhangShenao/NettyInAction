package ch08;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 
 * <p>Description:引导服务器</p>
 * @author ZhangShenao
 * @date 2017年9月22日
 */
public class BootstrapServer {
	public static void main(String[] args) {
		BootstrapServer server = new BootstrapServer();
		server.bootstrap();
	}
	public void bootstrap(){
		//创建ServerBootstrap
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		
		//设置EventLoopGroup,其提供了用于处理Channel事件的EventLoop
		EventLoopGroup group = new NioEventLoopGroup();
		serverBootstrap.group(group)
		
		//指定要使用的Channel实现
		.channel(NioServerSocketChannel.class)
		
		//设置用于处理已被接收的子Channel的I/O及数据的ChannelInboundHandler
		.childHandler(new SimpleChannelInboundHandler<ByteBuf>() {

			@Override
			protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
					throws Exception {
				System.err.println("Received data");
			}
		});
		
		//通过配置好的ServerBootstrap实例绑定该Channel
		ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(8080));
		
		future.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()){
					System.err.println("Server bounded");
				}
				else {
					System.err.println("Bound attempt failed");
					future.cause().printStackTrace();
				}
			}
		});
	}
}
