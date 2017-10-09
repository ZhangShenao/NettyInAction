package ch10;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * 
 * <p>Description:将出站数据从一种消息格式编码为另一种,继承MessageToMessageEncoder</p>
 * @author ZhangShenao
 * @date 2017年10月9日
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Integer msg,
			List<Object> out) throws Exception {
		//将Integer类型的数据转换为String,并添加到List中
		out.add(String.valueOf(msg));
	}

}
