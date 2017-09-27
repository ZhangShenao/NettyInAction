package ch09;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
/**
 * 
 * <p>Description:测试FrameChunkDecoder</p>
 * @author ZhangShenao
 * @date 2017年9月27日
 */
public class FrameChunkDecoderTest {
	private static final int BUFFER_SIZE = 9;
	private static final int MAX_FRAME_SIZE = 3;
	
	@Test
	public void testFrameDecoded(){
		//创建一个ByteBuf,并写入指定字节
		ByteBuf byteBuf = Unpooled.buffer(BUFFER_SIZE);
		for (int i = 0;i < BUFFER_SIZE;i++){
			byteBuf.writeByte(i);
		}
		ByteBuf input = byteBuf.duplicate();
		
		//创建一个EmbeddedChannel,并安装一个FrameChunkDecoder
		EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(MAX_FRAME_SIZE));
		
		//写入2个字节,并断言会产生一个新帧
		assertTrue(channel.writeInbound(input.readBytes(2)));
		
		//写入一个4字节大小的帧,并捕获预期的TooLongFrameException
		try {
			channel.writeInbound(input.readBytes(4));
			
			//如果上面没有抛出异常,则断言失败
			Assert.fail();
		}catch (TooLongFrameException e){
			
		}
		
		//写入剩余的两个字节,并断言将产生一个有效帧
		assertTrue(channel.writeInbound(input.readBytes(3)));
		
		//将Channel标记为已完成状态
		assertTrue(channel.finish());
		
		//读取产生的消息,并且验证值
		ByteBuf read = channel.readInbound();
		assertEquals(byteBuf.readSlice(2),read);
		
		read = channel.readInbound();
		assertEquals(byteBuf.skipBytes(4).readSlice(3),read);
		
		read.release();
		byteBuf.release();
	}
}
