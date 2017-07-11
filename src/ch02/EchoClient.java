package ch02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/**
 * 
 * <p>Description:使用Netty实现的Echo客户端</p>
 * @author ZhangShenao
 * @date 2017年7月11日
 */
public class EchoClient {
	/**
	 * 将客户端连接到指定的服务器
	 * @param host ip地址
	 * @param port 端口号
	 */
	public void connect(String host,int port){
		EventLoopGroup group = null;
		
		try {
			//1.创建EventLoopGroup用来处理客户端事件,使用NIO类型
			group = new NioEventLoopGroup();
			
			//2.创建Bootstrap引导类
			Bootstrap b = new Bootstrap();
			
			b.group(group)
			
			//3.指定Channel类型为NIO类型
			.channel(NioSocketChannel.class)
			
			//4.指定要连接的远程服务器的地址
			.remoteAddress(new InetSocketAddress(host, port))
			
			//5.在每次创建一个新的连接时,向新的Channel的ChannelPipeline中添加一个EchoClientHandler实例
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new EchoClientHandler());
				}
			});
			
			//6.异步连接到远程服务器,并阻塞当前线程直到操作完成
			ChannelFuture f = b.connect().sync();
			
			//7.阻塞当前线程,直到Channel关闭
			f.channel().closeFuture().sync();
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (null != group){
				group.shutdownGracefully();
			}
		}
	}
	
	public static void main(String[] args) {
		EchoClient client = new EchoClient();
		client.connect("localhost", 8000);
	}
}
