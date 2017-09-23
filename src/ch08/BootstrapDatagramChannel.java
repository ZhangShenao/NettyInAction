package ch08;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * 
 * <p>Description:使用UDP协议,引导DatagramChannel</p>
 * @author ZhangShenao
 * @date 2017年9月23日
 */
public class BootstrapDatagramChannel {
	public static void main(String[] args) {
		BootstrapDatagramChannel client = new BootstrapDatagramChannel();
		client.bootstrap();
	}
	public void bootstrap(){
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new OioEventLoopGroup())
		
		//使用DatagramChannel类型
		.channel(OioDatagramChannel.class)
		
		//DatagramChannel类型的Channel使用DatagramPacket作为消息容器
		.handler(new SimpleChannelInboundHandler<DatagramPacket>() {

			@Override
			protected void channelRead0(ChannelHandlerContext ctx,
					DatagramPacket msg) throws Exception {
				System.err.println("Received Data");
			}
		});
		
		//UDP协议是无连接的,调用bind()方法
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(0));
		
		future.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
	                   System.out.println("Channel bound");
	               } 
				else {
	                   System.err.println("Bind attempt failed");
	                   future.cause().printStackTrace();
	               }
			}
		});
	}
}
