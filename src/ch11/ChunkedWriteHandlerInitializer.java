package ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * 
 * <p>Description:使用ChunkedStream逐块传输文件内容</p>
 * @author ZhangShenao
 * @date 2017年10月22日
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel>{
	private final File file;
	private final SslContext sslCtx;
	
	public ChunkedWriteHandlerInitializer(File file, SslContext sslCtx) {
		this.file = file;
		this.sslCtx = sslCtx;
	}


	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		//将SslHandler添加到ChannelPipeline中
		pipeline.addLast(new SslHandler(sslCtx.newEngine(ch.alloc())));
		
		//添加ChunkedWriterHandler以处理作为ChunkedInput传入的数据
		pipeline.addLast(new ChunkedWriteHandler());
		
		//一旦连接建立,WriteStreamHandler就开始传输文件数据
		pipeline.addLast(new WriteStreamHandler());
	}
	
	public final class WriteStreamHandler extends ChannelInboundHandlerAdapter{
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			//当建立连接时,使用ChunkedInput写文件数据
			super.channelActive(ctx);
			ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
		}
	}

}
