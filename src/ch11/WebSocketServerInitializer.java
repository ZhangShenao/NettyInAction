package ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 
 * <p>Description:WebSocket的服务端处理</p>
 * @author ZhangShenao
 * @date 2017年10月13日
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		//添加HTTP服务器编解码器
		ch.pipeline().addLast(new HttpServerCodec())
		
		//为握手提供聚合的HttpRequest
		.addLast(new HttpObjectAggregator(65535))
		
		//使用WebSocketServerProtocolHandler处理服务器的WebSocket协议
		//如果被请求的端点是"/websocket",则处理升级握手
		.addLast(new WebSocketServerProtocolHandler("/websocket"))
		
		//TextFrameHandler处理TextWebSocketFrame
		.addLast(new TextFrameHandler())
		
		//BinaryFrameHandler处理BinaryWebSocketFrame
		.addLast(new BinaryFrameHandler())
		
		//ContinuationFrameHandler处理ContinuationWebSocketFrame
		.addLast(new ContinuationFrameHandler());
	}
	
	/**
	 * 
	 * <p>Description:文本帧处理器,处理TextWebSocketFrame</p>
	 * @author ZhangShenao
	 * @date 2017年10月13日
	 */
	public static final class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
		@Override
		protected void channelRead0(ChannelHandlerContext ctx,
				TextWebSocketFrame msg) throws Exception {
			
		}
		
	}
	
	/**
	 * 
	 * <p>Description:二进制帧处理器,处理BinaryWebSocketFrame</p>
	 * @author ZhangShenao
	 * @date 2017年10月13日
	 */
	public static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame>{
		@Override
		protected void channelRead0(ChannelHandlerContext ctx,
				BinaryWebSocketFrame msg) throws Exception {
		}
		
	}
	
	/**
	 * 
	 * <p>Description:Continuation帧处理器,处理ContinuationWebSocketFrame</p>
	 * @author ZhangShenao
	 * @date 2017年10月13日
	 */
	public static final class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame>{
		@Override
		protected void channelRead0(ChannelHandlerContext ctx,
				ContinuationWebSocketFrame msg) throws Exception {
		}
		
	}



}
