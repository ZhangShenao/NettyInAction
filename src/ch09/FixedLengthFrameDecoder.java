package ch09;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 
 * <p>Description:生成固定大小的帧的解码器</p>
 * <p>扩展ByteToMessageDecoder以处理入站字节,并将他们处理成消息</p>
 * @author ZhangShenao
 * @date 2017年9月26日
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder{
	private final int frameLength;		//指定要生成的帧的固定长度
	
	public FixedLengthFrameDecoder(int frameLength) {
		if (frameLength <= 0){
			 throw new IllegalArgumentException(
		                "frameLength must be a positive integer: " + frameLength);
		}
		this.frameLength = frameLength;
	}


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		//检查是否有足够的字节可以被读取,以生成下一个帧
		while (in.readableBytes() >= frameLength){
			//从入站字节中读取一个新帧
			ByteBuf byteBuf = in.readBytes(frameLength);
			
			//将该新帧添加到已被解码的消息列表中
			out.add(byteBuf);
		}
	}

}
