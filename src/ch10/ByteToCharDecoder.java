package ch10;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 
 * <p>Description:将字节解码为Charater类型的解码器</p>
 * @author ZhangShenao
 * @date 2017年10月10日
 */
public class ByteToCharDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		//将一个或多个Character对象添加到传出的List中
		while (in.readableBytes() >= 2){
			out.add(in.readChar());
		}
	}

}
