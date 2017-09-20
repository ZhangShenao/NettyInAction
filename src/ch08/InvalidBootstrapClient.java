package ch08;

import java.net.InetSocketAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.oio.OioSocketChannel;

/**
 * 
 * <p>Description:不兼容的Channel和EventLoopGroup</p>
 * @author ZhangShenao
 * @date 2017年9月20日
 */
public class InvalidBootstrapClient {
	public static void main(String[] args) {
		InvalidBootstrapClient client = new InvalidBootstrapClient();
		client.bootstrap();
	}
	public void bootstrap(){
		//指定一个适用于NIO的EventLoopGroup实现
		EventLoopGroup group = new NioEventLoopGroup();
		
		//创建客户端引导Bootstrap实例
		Bootstrap bootstrap = new Bootstrap();
		
		bootstrap.group(group)
		
		//指定一个使用OIO的Channel实现类
		.channel(OioSocketChannel.class)
		
		.handler(new SimpleChannelInboundHandler<ByteBuf>() {

			@Override
			protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
					throws Exception {
				System.err.println("Received Data");
			}
		});
		
		// 尝试连接到远程节点,将导致java.lang.IllegalStateException异常,因为混用了不兼容的传输
		ChannelFuture future = bootstrap.connect(
                new InetSocketAddress("www.manning.com", 80));
        future.syncUninterruptibly();
	}
}
