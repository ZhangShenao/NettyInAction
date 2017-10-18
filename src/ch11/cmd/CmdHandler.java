package ch11.cmd;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 
 * <p>Description:Cmd帧处理器,从CmdDecoder中得到解码的Cmd帧并进行一些处理</p>
 * @author ZhangShenao
 * @date 2017年10月18日
 */
public class CmdHandler extends SimpleChannelInboundHandler<Cmd>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Cmd msg)
			throws Exception {
		//处理创建ChannelPipeline的Cmd对象
		System.err.println(msg);
	}

}
