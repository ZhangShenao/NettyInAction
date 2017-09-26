package ch09;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * <p>Description:测试FixedLengthFrameDecoder,入站消息</p>
 * @author ZhangShenao
 * @date 2017年9月26日
 */
public class FixedLengthFrameDecoderTest {
	private static final int DECODER_LENGTH = 3;
	private static final int TOTAL_BYTES = 9;
	@Test
	public void testFrameDecoder(){
		//创建一个ByteBuf,并存储9个字节
		ByteBuf byteBuf = Unpooled.buffer(TOTAL_BYTES);
		
		for (int i = 0;i < TOTAL_BYTES;i++){
			byteBuf.writeByte(i);
		}
		ByteBuf input = byteBuf.duplicate();
		
		//创建一个EmbeddedChannel,并添加一个FixedLengthFrameDecoder,其将以3字节的帧长度被测试
		EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(DECODER_LENGTH));
		
		//将数据写入EmbeddedChannel
		assertTrue(channel.writeInbound(input.retain()));
		
		//标记Channel为以完成状态
		assertTrue(channel.finish());
		
		//读取生成的消息,并且验证是否有3帧(切片),其中每帧(切片)都为3个字节
		for (int i = 0;i < TOTAL_BYTES / DECODER_LENGTH;i++){
			ByteBuf read = channel.readInbound();
			assertEquals(byteBuf.readSlice(DECODER_LENGTH), read);
			read.release();
		}
		
		assertNull(channel.readInbound());
		byteBuf.release();
	}
	
	@Test
	public void testFrameDecoder2(){
		//创建一个ByteBuf,并存储9个字节
		ByteBuf byteBuf = Unpooled.buffer(TOTAL_BYTES);
		
		for (int i = 0;i < TOTAL_BYTES;i++){
			byteBuf.writeByte(i);
		}
		ByteBuf input = byteBuf.duplicate();
		
		//创建一个EmbeddedChannel,并添加一个FixedLengthFrameDecoder,其将以3字节的帧长度被测试
		EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(DECODER_LENGTH));
		
		//返回false,因为没有一个完整的可供读取的帧
		assertTrue(channel.writeInbound(input.readBytes(2)));
		assertTrue(channel.writeInbound(input.readBytes(7)));
		
		//标记Channel为以完成状态
		assertTrue(channel.finish());
		
		//读取生成的消息,并且验证是否有3帧(切片),其中每帧(切片)都为3个字节
		for (int i = 0;i < TOTAL_BYTES / DECODER_LENGTH;i++){
			ByteBuf read = channel.readInbound();
			assertEquals(byteBuf.readSlice(DECODER_LENGTH), read);
			read.release();
		}
		
		assertNull(channel.readInbound());
		byteBuf.release();
	}
}
