package ch10;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 
 * <p>Description:ByteToMessageDecoder解码器</p>
 * <p>自定义解码器扩展ByteToMessageDecoder类,以将字节解码为特定的格式</p>
 * @author ZhangShenao
 * @date 2017年9月29日
 */
public class ToIntegerDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		//检查是否至少有4个字节可读(1个int的长度)
		if (in.readableBytes() < 4){
			return;
		}
		
		//从入站ByteBuf中读取一个int,并将其添加到解码消息的List中
		out.add(in.readInt());
	}

}
