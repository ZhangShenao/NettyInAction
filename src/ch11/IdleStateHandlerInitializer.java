package ch11;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

/**
 * 
 * <p>Description:发送心跳消息</p>
 * @author ZhangShenao
 * @date 2017年10月17日
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		//IdleStateHandler将在被触发时发送一个IdleStateEvent事件
		pipeline.addLast(new IdleStateHandler(0, 0, 60,TimeUnit.SECONDS))
		
		//添加HeartbeatHandler,处理IdleStateEvent事件
		.addLast(new HeartbeatHandler());
	}
	
	
	public static final class HeartbeatHandler extends ChannelInboundHandlerAdapter{
		//发送到远程节点的心跳消息
		private static final ByteBuf HEARTBEAT_SEQUENCE = 
				Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.ISO_8859_1));
		
		//实现userEventTriggered方法已发送心跳消息
		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
				throws Exception {
			if (evt instanceof IdleStateEvent){
				//如果是IdleStateEvent,则发送心跳消息,并在发送失败时关闭该连接
				ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
				.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
			}
			else {
				//如果不是IdleStateEvent,就将该事件传递给下一个ChannelInboundHandler
				super.userEventTriggered(ctx, evt);
			}
		}
	}
}
