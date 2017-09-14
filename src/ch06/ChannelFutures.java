package ch06;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * <p>Description:添加ChannelFutureListener到ChannelFuture</p>
 * @author ZhangShenao
 * @date 2017年9月14日
 */
public class ChannelFutures {
	private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final ByteBuf SOME_MSG_FROM_SOMEWHERE = Unpooled.buffer(1024);
    
    /**
     * 添加 ChannelFutureListener 到 ChannelFuture
     */
    public void addingChannelFutureListener(){
    	Channel channel = CHANNEL_FROM_SOMEWHERE;
    	ChannelFuture future = channel.write(SOME_MSG_FROM_SOMEWHERE);
    	
    	future.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (!future.isSuccess()){
					future.cause().printStackTrace();
					future.channel().close();
				}
			}
		});
    }
}
