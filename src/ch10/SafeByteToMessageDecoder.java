package ch10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 
 * <p>Description:当需解码的字节长度超过限制时,抛出TooLongFrameException</p>
 * @author ZhangShenao
 * @date 2017年9月30日
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder{
	private static final int MAX_FRAME_SIZE = 1024;		//可解码的最大字节数
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		//检查缓冲区是否有超过MAX_FRAME_SIZE个字节
		int readableBytes = in.readableBytes();
		
		//跳过所有可读字节,抛出TooLongFrameException,并通知
		if (readableBytes > MAX_FRAME_SIZE){
			in.skipBytes(readableBytes);
			throw new TooLongFrameException("Frame too big!!");
		}
	}

}
