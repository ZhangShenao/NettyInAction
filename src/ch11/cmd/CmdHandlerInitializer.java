package ch11.cmd;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class CmdHandlerInitializer extends ChannelInitializer<Channel>{
	public static final byte SPACE = (byte)' ';		//命令名称和命令参数之间的分隔符

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		//添加CmdDecoder以提取Cmd对象,并将它转发给下一个ChannelInboundHandler
		pipeline.addLast(new CmdDecoder(64 * 1024));
		
		//添加CmdHandler以接收和处理Cmd对象
		pipeline.addLast(new CmdHandler());
	}
}
