package ch08;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * <p>Description:使用NIO TCP传输的客户端</p>
 * @author ZhangShenao
 * @date 2017年9月20日
 */
public class BootstrapClient {
	public static void main(String[] args) {
		BootstrapClient client = new BootstrapClient();
		client.bootstrap();
	}
	/**
	 * 引导客户端
	 */
	public void bootstrap(){
		//创建EventLoopGroup
		EventLoopGroup group = new NioEventLoopGroup();
		
		//创建一个Bootstrap实例,以创建和连接新的客户端Channel
		Bootstrap bootstrap = new Bootstrap();
		
		//指定EventLoopGroup,提供用于处理Channel事件的EventLoop
		bootstrap.group(group)
		
		//指定要使用的Channel实现
		.channel(NioSocketChannel.class)
		
		//设置用于Channel事件和数据的ChannelInboundHandler
		.handler(new SimpleChannelInboundHandler<ByteBuf>() {

			@Override
			protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
					throws Exception {
				System.err.println("Receive Data");
			}
		});
		
		//连接到远程主机
		ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));
		
		future.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()){
					System.err.println("Connection established");
					future.channel().close();
				}
				else {
					System.out.println("Connection attempt failed");
					future.cause().printStackTrace();
				}
			}
		});
	}
}
