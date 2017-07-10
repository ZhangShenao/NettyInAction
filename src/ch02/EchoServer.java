package ch02;

import java.net.InetSocketAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 
 * <p>Description:使用Netty实现的Echo服务器类</p>
 * @author ZhangShenao
 * @date 2017年7月10日
 */
public class EchoServer {
	/**
	 * 启动服务器
	 * @param port 服务器监听的端口号
	 */
	public void service(int port){
		//创建EchoServerHandler
		final EchoServerHandler echoServerHandler = new EchoServerHandler();
		EventLoopGroup group = null;
		
		try {
			//1.创建EventLoopGroup
			//EventLoopGroup实例主要用来进行事件的处理,如接受新的连接和读、写数据等
			group = new NioEventLoopGroup();
			
			//2.创建ServerBootstrap
			 ServerBootstrap b = new ServerBootstrap();
			 
			 b.group(group)
			 
			 //3.指定所使用的Channel类型为NIO类型
			 .channel(NioServerSocketChannel.class)
			 
			 //4.使用指定的端口设置套接字地址
			 .localAddress(new InetSocketAddress(port))
			 
			 //5.添加一个EchoServerHandler到子Channel的 ChannelPipeline
             //当一个新的连接被接受时,一个新的子Channel将被创建,而ChannelInitializer将会把一个EchoServerHandler实例添加到该Channel的ChannelPipeline中
			 .childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(echoServerHandler);
					
				}
			});
			 
			 //6.将服务器异步绑定到监听端口,并阻塞直到操作完成
			 ChannelFuture f = b.bind(port).sync();
			 System.out.println(EchoServer.class.getName() +
		                " started and listening for connections on " + f.channel().localAddress());
			 
			 //7.获取 Channel的CloseFuture，并且阻塞当前线程直到它完成
			 f.channel().closeFuture().sync();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (null != group){
				group.shutdownGracefully();
			}
		}
	}
}
