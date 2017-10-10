package ch10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 
 * <p>Description:将Character编码为字节的编码器</p>
 * @author ZhangShenao
 * @date 2017年10月10日
 */
public class CharToByteEncoder extends MessageToByteEncoder<Character>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out)
			throws Exception {
		//将Character解码为Byte,并将其写入到出战ByteBuf中
		out.writeChar(msg);
	}

}
