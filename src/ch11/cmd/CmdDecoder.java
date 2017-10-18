package ch11.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;


/**
 * 
 * <p>Description:Cmd帧解码器,从被重写的decode()方法中获取一行字符串,并根据它的内容构建一个Cmd实例</p>
 * @author ZhangShenao
 * @date 2017年10月18日
 */
public class CmdDecoder extends LineBasedFrameDecoder{
	public CmdDecoder(int maxLength) {
		super(maxLength);
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer)
			throws Exception {
		//从ByteBuf中提取由行尾符序列分隔的帧
		ByteBuf frame = (ByteBuf)super.decode(ctx, buffer);
		
		//如果输入中没有帧,则返回null
		if (null == frame){
			return null;
		}
		
		//查找第一个空格字符的索引,前面是命令名称,后面是命令参数
		int spaceIndex = frame.indexOf(frame.readerIndex(), frame.writerIndex(), CmdHandlerInitializer.SPACE);
		if (spaceIndex < 0){
			return null;
		}
		
		//解析命令名称和命令参数
		ByteBuf cmdName = frame.slice(frame.readerIndex(), spaceIndex);
		ByteBuf cmdArgs = frame.slice(spaceIndex + 1, frame.writerIndex());
		
		//创建新的Cmd对象并返回
		return new Cmd(cmdName, cmdArgs);
	}

}
