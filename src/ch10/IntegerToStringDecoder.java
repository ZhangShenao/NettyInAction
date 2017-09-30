package ch10;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 
 * <p>Description:使用MessageToMessageDecoder,将Integer类型转换为String类型</p>
 * @author ZhangShenao
 * @date 2017年9月30日
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer>{

	@Override
	protected void decode(ChannelHandlerContext ctx, Integer msg,
			List<Object> out) throws Exception {
		//将Integer消息转换为它的String表示,并将其添加到输出的List中
		out.add(String.valueOf(msg));
	}

}
