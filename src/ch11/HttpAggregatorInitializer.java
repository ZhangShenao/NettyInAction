package ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 
 * <p>Description:使用HttpObjectAggregator,自动聚合Http的消息片段</p>
 * @author ZhangShenao
 * @date 2017年10月11日
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel>{
	private final boolean isClient;
	
	public HttpAggregatorInitializer(boolean isClient) {
		this.isClient = isClient;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		if (isClient){
			//如果是客户端,则添加HttpClientCodec
			ch.pipeline().addLast("codec", new HttpClientCodec());
		}
		else {
			//如果是服务器,则添加HttpServerCodec
			ch.pipeline().addLast("codec", new HttpServerCodec());
		}
		
		//添加HttpObjectAggregator,将多个消息合并为FullHttpRequest或FullHttpResponse
		ch.pipeline().addLast("aggregator",new HttpObjectAggregator(512 * 1024));	//最大消息大小为512k
	}

}
