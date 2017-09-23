package ch08;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * 
 * <p>Description:使用属性值</p>
 * @author ZhangShenao
 * @date 2017年9月23日
 */
public class BootstrapClientWithOptionsAndAttrs {
	public static void main(String[] args) {
		BootstrapClientWithOptionsAndAttrs client = new BootstrapClientWithOptionsAndAttrs();
		client.bootstrap();
	}
	public void bootstrap(){
		//创建一个AttributeKey以标识该属性
		final AttributeKey<Integer> id = AttributeKey.newInstance("ID");
		
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup())
		.channel(NioSocketChannel.class)
		.handler(new SimpleChannelInboundHandler<ByteBuf>() {

			@Override
			protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
					throws Exception {
				System.err.println("Received Data");
			}
			
			@Override
			public void channelRegistered(ChannelHandlerContext ctx)
					throws Exception {
				//使用AttributeKey检索属性以及他的值
				Integer idValue = ctx.channel().attr(id).get();
				System.err.println("idValue: " + idValue);
			}
		});
		
		//设置ChannelOption,其将在connect()或bind()方法被调用时被设置到已经创建的Channel上
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000);
		
		//存储该属性值
		bootstrap.attr(id, 123456);
		
		ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));
		future.syncUninterruptibly();
	}
}
