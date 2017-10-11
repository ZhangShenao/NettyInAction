package ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 
 * <p>Description:HTTP压缩</p>
 * @author ZhangShenao
 * @date 2017年10月11日
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel>{
	private final boolean isClient;
	
	public HttpCompressionInitializer(boolean isClient) {
		this.isClient = isClient;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		if (isClient){
			//如果是客户端,则添加HttpClientCodec
			pipeline.addLast("codec", new HttpClientCodec());
			
			//如果是客户端,则添加HttpContentDecompressor以处理来自服务器的压缩内容
			pipeline.addLast("decompressor", new HttpContentDecompressor());
		}
		else {
			//如果是服务器,则添加HttpServerCodec
			pipeline.addLast("codec", new HttpServerCodec());
			
			//如果是服务器,则添加HttpContentCompressor来压缩数据(如果客户端支持它)
			pipeline.addLast("compressor", new HttpContentCompressor());
		}
		
		//添加HttpObjectAggregator,将多个消息合并为FullHttpRequest或FullHttpResponse
		ch.pipeline().addLast("aggregator",new HttpObjectAggregator(512 * 1024));	//最大消息大小为512k
	}

}
