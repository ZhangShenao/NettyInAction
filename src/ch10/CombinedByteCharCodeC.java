package ch10;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * 
 * <p>Description:Character和Byte类型互相转换的编解码器</p>
 * @author ZhangShenao
 * @date 2017年10月10日
 */
public class CombinedByteCharCodeC extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder>{
	public CombinedByteCharCodeC(){
		//将委托实例传递给父类
		super(new ByteToCharDecoder(), new CharToByteEncoder());
	}
}
