package ch09;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

/**
 * 
 * <p>Description:限制帧最大字节长度的解码器</p>
 * @author ZhangShenao
 * @date 2017年9月27日
 */
public class FrameChunkDecoder extends ByteToMessageDecoder{
	private final int maxFrameSize;
	
	
	public FrameChunkDecoder(int maxFrameSize) {
		//指定将要产生的帧的最大允许大小
		this.maxFrameSize = maxFrameSize;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		//检验帧的长度
		int readableBytes = in.readableBytes();
		
		//如果该帧太大,则丢弃它,并抛出TooLongFrameException
		if (readableBytes > maxFrameSize){
			in.clear();
			throw new TooLongFrameException();
		}
		
		//否则,从ByteBuf中读取一个新的帧
		ByteBuf buf = in.readBytes(readableBytes);
		
		//将该帧添加到解码消息的List中
		out.add(buf);
	}

}
