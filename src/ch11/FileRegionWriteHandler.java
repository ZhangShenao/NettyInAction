package ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;

/**
 * 
 * <p>Description:使用FileRegion传输文件的内容</p>
 * @author ZhangShenao
 * @date 2017年10月20日
 */
public class FileRegionWriteHandler extends ChannelInboundHandlerAdapter{
	private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final File FILE_FROM_SOMEWHERE = new File("");
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	File file = FILE_FROM_SOMEWHERE;
    	Channel channel = CHANNEL_FROM_SOMEWHERE;
    	
    	//创建FileInputStream
    	final FileInputStream in = new FileInputStream(file);
    	
    	//以该文件完整的长度创建一个新的DefauleFileRegion
    	FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());
    	
    	//发送该DefaultFileRegion,并注册ChannelFutureListener
    	channel.writeAndFlush(region).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				//失败处理
				if (!future.isSuccess()){
					Throwable cause = future.cause();
					cause.printStackTrace();
				}
				else {
					in.close();
				}
			}
		});
    }
}
