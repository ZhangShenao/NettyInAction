package ch11;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

/**
 * 
 * <p>Description:使用HTTPS</p>
 * @author ZhangShenao
 * @date 2017年10月11日
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel>{
	private final SslContext context;
	private final boolean isClient;
	
	public HttpsCodecInitializer(SslContext context, boolean isClient) {
		this.context = context;
		this.isClient = isClient;
	}


	@Override
	protected void initChannel(Channel ch) throws Exception {
		//将SslHandler添加到ChannelPipeline中以使用HTTPS
		ChannelPipeline pipeline = ch.pipeline();
		SSLEngine engine = context.newEngine(ch.alloc());
		pipeline.addLast("SSL", new SslHandler(engine));
		
		if (isClient){
			//如果是客户端,则添加HttpClientCodec
			pipeline.addLast("codec", new HttpClientCodec());
		}
		else {
			//如果是服务器,则添加HttpServerCodec
			pipeline.addLast("codec", new HttpServerCodec());
		}
	}

}
