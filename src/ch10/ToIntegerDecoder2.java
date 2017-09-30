package ch10;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * 
 * <p>Description:使用ReplayingDecoder</p>
 * <p>扩展ReplayingDecoder,以将字节解码为消息</p>
 * @author ZhangShenao
 * @date 2017年9月30日
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void>{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,	
			List<Object> out) throws Exception {
		//传入的ByteBuf是ReplayingDecoderByteBuf,在readInt()时会对可读字节数进行校验
		out.add(in.readInt());
	}

}
