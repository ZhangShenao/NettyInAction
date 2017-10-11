package ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 
 * <p>Description:添加HTTP支持</p>
 * @author ZhangShenao
 * @date 2017年10月11日
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel>{
	private final boolean isClient;	//是否是客户端
	
	public HttpPipelineInitializer(boolean isClient) {
		this.isClient = isClient;
	}


	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		if (isClient){
			//如果是客户端,则添加HttpResponseDecoder以解析来自服务器的响应
			pipeline.addLast("decoder", new HttpResponseDecoder());
			
			//如果是客户端,则添加HttpRequestEncoder以向服务器发送请求
			pipeline.addLast("encoder", new HttpRequestEncoder());
		}
		
		else {
			//如果是服务器,则添加HttpRequestDecoder以解析来自客户端的请求
			pipeline.addLast("decoder", new HttpRequestDecoder());
			
			//如果是服务器,则添加HttpResponseEncoder以向客户端发送响应
			pipeline.addLast("encoder", new HttpResponseEncoder());
		}
	}
	
}
