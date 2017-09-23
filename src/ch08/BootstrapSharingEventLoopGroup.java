package ch08;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class BootstrapSharingEventLoopGroup {
	public void bootstrap(){
		//创建ServerBootstrap
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		
		//设置EventLoopGroup
		serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup());
		
		//设置要使用的Channel实现
		serverBootstrap.channel(NioServerSocketChannel.class)
		
		//设置用于处理已被接受的子Channel的I/O和数据的ChannelInboundHandler
		.childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
			ChannelFuture connectFuture = null;
			
			@Override
			protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
					throws Exception {
				
			}
			
			@Override
			public void channelActive(ChannelHandlerContext ctx)
					throws Exception {
				//创建一个Bootstrap类的实例以连接到远程主机
				Bootstrap bootstrap = new Bootstrap();
				
				//指定Channel实现
				bootstrap.channel(NioSocketChannel.class)
				
				//为入站的I/O设置ChannelInboundHandler
				.handler(new SimpleChannelInboundHandler<ByteBuf>() {

					@Override
					protected void channelRead0(ChannelHandlerContext ctx,
							ByteBuf msg) throws Exception {
						System.out.println("Received Data");
					}
				});
				
				//使用分配给已被接收的子Channel相同的EventLoop
				bootstrap.group(ctx.channel().eventLoop());
				
				//连接到远程节点
				connectFuture = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));
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
