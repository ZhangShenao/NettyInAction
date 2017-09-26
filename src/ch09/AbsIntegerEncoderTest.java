package ch09;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import static org.junit.Assert.*;
/**
 * 
 * <p>Description:测试AbsIntegerEncoder,出站消息</p>
 * @author ZhangShenao
 * @date 2017年9月26日
 */
public class AbsIntegerEncoderTest {
	private static final int INTEGER_NUM = 9;
	
	@Test
	public void testEncode(){
		//创建一个ByteBuf,并写入9个负整数
		ByteBuf byteBuf = Unpooled.buffer(INTEGER_NUM);
		for (int i = 1;i <= INTEGER_NUM;i++){
			byteBuf.writeInt(-1 * i);
		}
		
		//创建一个EmbeddedChannel,并安装要测试的AbsIntegerEncoder
		EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
		
		//写入ByteBuf,并断言调用readOutbound()方法将产生数据
		assertTrue(channel.writeOutbound(byteBuf));
		
		//将Channel标记为已完成状态
		assertTrue(channel.finish());
		
		//读取所产生的消息,并断言它们包含了对应的绝对值
		for (int i = 1;i <= INTEGER_NUM;i++){
			assertEquals(i, channel.readOutbound());
		}
		
		assertNull(channel.readOutbound());
	}
}
