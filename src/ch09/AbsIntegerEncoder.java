package ch09;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * 
 * <p>Description:将整数转换为其绝对值的编码器</p>
 * @author ZhangShenao
 * @date 2017年9月26日
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf>{
	private final int BYTES = 4;
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		//检查是否有足够的字节进行编码
		while (msg.readableBytes() >= BYTES){
			//从输入的ByteBuf中读取下一个整数,并计算其绝对值
			int value = msg.readInt();
			
			//将计算后的结果放入编码消息的List中
			out.add(Math.abs(value));
		}
	}

}
