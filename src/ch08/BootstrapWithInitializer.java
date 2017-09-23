package ch08;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

/**
 * 
 * <p>Description:引导和使用ChannelIniatializer</p>
 * @author ZhangShenao
 * @date 2017年9月23日
 */
public class BootstrapWithInitializer {
	public void bootstrap() throws InterruptedException{
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(new NioEventLoopGroup(),new NioEventLoopGroup())
		.channel(NioServerSocketChannel.class)
		
		//注册一个ChannelInitializerImpl的实例来设置ChannelPipeline
		.childHandler(new ChannelInitializerImpl());
		
		ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(8080));
		future.sync();
	}
	
	/**
	 * 
	 * <p>Description:用以设置ChannelPipeline的ChannelInitializer的实现类</p>
	 * @author ZhangShenao
	 * @date 2017年9月23日
	 */
	final class ChannelInitializerImpl extends ChannelInitializer<Channel>{
		//将所需的ChannelHandler添加到ChannelPipeline
		@Override
		protected void initChannel(Channel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new HttpClientCodec());
			pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
		}
		
	}
}
