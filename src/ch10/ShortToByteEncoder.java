package ch10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 
 * <p>Description:自定义编码器,继承MessageToByteEncoder</p>
 * @author ZhangShenao
 * @date 2017年10月9日
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out)
			throws Exception {
		//将short数据写入ByteBuf中
		out.writeShort(msg)	;
	}

}
