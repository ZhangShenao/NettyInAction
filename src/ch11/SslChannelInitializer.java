package ch11;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

/**
 * 
 * <p>Description:通过SSL/TLS保护Netty数据</p>
 * @author ZhangShenao
 * @date 2017年10月11日
 */
public class SslChannelInitializer extends ChannelInitializer<Channel>{
	private final SslContext context;
	private final boolean startTls;
	
	/**
	 * 构造SslChannelInitializer
	 * @param context 需要使用的SslContext
	 * @param startTls 如果设置为 true，第一个写入的消息将不会被加密(客户端应该设置为 true)
	 */
	public SslChannelInitializer(SslContext context, boolean startTls) {
		this.context = context;
		this.startTls = startTls;
	}


	@Override
	protected void initChannel(Channel ch) throws Exception {
		//对于每个 SslHandler 实例，都使用 Channel 的 ByteBufAllocator 从 SslContext 获取一个新的 SSLEngine
		SSLEngine engine = context.newEngine(ch.alloc());
		
		//将 SslHandler 作为第一个 ChannelHandler 添加到 ChannelPipeline中
		ch.pipeline().addFirst("SSL",new SslHandler(engine, startTls));
	}

}
