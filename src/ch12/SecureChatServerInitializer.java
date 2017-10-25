package ch12;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

/**
 * 
 * <p>Description:  加密的ChatServerInitializer</p>
 * <p>扩展ChatServerInitializer以实现加密</p>
 * @author ZhangShenao
 * @date 2017年10月25日 下午11:08:56
 */
public class SecureChatServerInitializer extends ChatServerInitializer{
	private final SslContext sslCtx;
	
	public SecureChatServerInitializer(ChannelGroup group,SslContext sslCtx) {
		super(group);
		this.sslCtx = sslCtx;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		super.initChannel(ch);
		
		//将SslHandler添加到ChannelPipeline中
		SSLEngine engine = sslCtx.newEngine(ch.alloc());
		engine.setUseClientMode(false);
		ch.pipeline().addLast(new SslHandler(engine));
	}
}
