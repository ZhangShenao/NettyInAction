package ch12;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 
 * <p>Description:  处理TextWebSocketFrame消息</p>
 * @author ZhangShenao
 * @date 2017年10月25日 上午7:38:16
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
	private final ChannelGroup group;	//保存所有已连接的WebSocket的客户端的Channel
	
	public TextWebSocketFrameHandler(ChannelGroup group) {
		this.group = group;
	}
	
	//重写userEventTriggered方法已处理自定义事件
	@SuppressWarnings("deprecation")
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		//如果该事件为WebSocket握手成功事件
		if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
			onWebSocketHandshakeSucc(ctx);
			return;
		}
		
		//如果是其他事件,则交给下一个ChannelInboundHandler处理
		ctx.fireUserEventTriggered(evt);
	}
	
	/**
	 * 处理WebSocket的握手成功事件
	 */
	private void onWebSocketHandshakeSucc(ChannelHandlerContext ctx){
		//从ChannelPipeline中删除HttpRequesyHandler,因为不会接受任何Http消息了
		ctx.pipeline().remove(HttpRequestHandler.class);
		
		//通知所有已连接的WebSocket客户端,新的客户端已经连接上了
		group.writeAndFlush(new TextWebSocketFrame("client " + ctx.channel() + " joined"));
		
		//将新的WebSocket的Channel添加到ChannelGroup中,以便它可以接收到所有消息
		group.add(ctx.channel());
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception {
		//当接收到新消息时,增加消息的引用次数,并将消息写入ChannelGroup中所有已连接的客户端
		group.writeAndFlush(msg.retain());
	}

}
