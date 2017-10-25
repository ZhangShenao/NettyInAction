package ch12;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * 
 * <p>Description:  聊天服务器</p>
 * @author ZhangShenao
 * @date 2017年10月25日 上午8:05:44
 */
public class ChatServer {
	//创建DefaultChannelGroup,其将保存所有已经连接的 WebSocket Channel
	private final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	
	//创建两个EventLoopGroup
	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private Channel channel;
	
	public static final int DEFAULT_PORT = 8000;
	
	/**
	 * 启动服务器
	 */
	public ChannelFuture start(InetSocketAddress address){
		//引导服务器
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		.childHandler(createChildInitializer(group));
		ChannelFuture future = bootstrap.bind(address);
		future.syncUninterruptibly();
		channel = future.channel();
		System.err.println("ChatServer start at " + address);
		return future;
	}
	
	/**
	 * 关闭服务器,并释放所有资源
	 */
	public void close(){
		if (null != channel){
			channel.close();
		}
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
	
	/**
	 * 创建ChatServerInitializer
	 */
	protected ChannelInitializer<Channel> createChildInitializer(ChannelGroup group){
		return new ChatServerInitializer(group);
	}
	
	public static void main(String[] args) {
		if (args.length < 1){
			System.err.println("Please give port as argument");
            System.exit(1);
		}
		
		int port = DEFAULT_PORT;
		try {
			port = Integer.parseInt(args[0]);
		}catch (Exception e){
			System.err.println("port must be a integer");
            System.exit(1);
		}
		
		final ChatServer endpoint = new ChatServer();
		ChannelFuture future = endpoint.start(new InetSocketAddress(port));
		
		//设置关闭服务器的钩子
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				endpoint.close();
			}
		});
		
		future.channel().closeFuture().syncUninterruptibly();
	}
}
