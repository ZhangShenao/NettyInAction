package ch05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.util.Random;

import org.junit.Test;

/**
 * 
 * <p>Description:ByteBuf的字节级操作</p>
 * @author ZhangShenao
 * @date 2017年9月8日
 */
public class ByteBufOperations {
	private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);
	private static final Random random = new Random(47);
	
	/**
	 * 随机访问索引
	 * ByteBuf的索引从0开始,最后一个索引是capacity()-1
	 */
	@Test
	public void byteBufRelativeAccess(){
		ByteBuf byteBuf = BYTE_BUF_FROM_SOMEWHERE;
		for (int i = 0;i < 10;i++){
			byteBuf.writeChar('A' + i);
		}
		
		//使用索引访问数据,并不会移动readerIndex和writerIndex
		for (int i = 0,capacity = byteBuf.capacity();i < capacity;i++){
			byte b = byteBuf.getByte(i);
			System.out.print((char)b);
		}
	}
	
	/**
	 * 读取所有数据
	 */
	@Test
	public void readAllData(){
		ByteBuf byteBuf = BYTE_BUF_FROM_SOMEWHERE;
		for (int i = 0;i < 10;i++){
			byteBuf.writeChar('A' + i);
		}
		
		while (byteBuf.isReadable()){
			System.out.print((char)byteBuf.readByte());
		}
	}
	
	/**
	 * 写数据
	 */
	@Test
	public void writeData(){
		ByteBuf byteBuf = BYTE_BUF_FROM_SOMEWHERE;
		
		while (byteBuf.writableBytes() >= 4){
			byteBuf.writeInt(random.nextInt(100));
		}
		
		while (byteBuf.isReadable()){
			System.out.print(byteBuf.readInt() + ",");
		}
		
		System.out.println();
	}
	
	/**
	 * 使用ByteBufProcessor来查找\r
	 */
	@Test
	public void byteProcessor(){
		ByteBuf byteBuf = BYTE_BUF_FROM_SOMEWHERE;
		int index = byteBuf.forEachByte(ByteBufProcessor.FIND_CR);
		System.out.println(index);
	}
	
	/**
	 * 对ByteBuf进行切片
	 * 派生缓冲区与源缓冲区的数据是共享的
	 */
	@Test
	public void byteBufSlice(){
		Charset utf8 = Charset.forName("UTF-8");
		ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!!", utf8);
		
		//创建该ByteBuf的从索引0开始到索引15的新切片
		ByteBuf sliced = byteBuf.slice(0, 15);
		System.out.println(sliced.toString(utf8));
		
		//修改派生缓冲区中的数据,源缓冲区的数据也会同步修改
		sliced.setByte(0, (byte)'J');
		
		System.out.println((char)sliced.getByte(0));
		System.out.println((char)byteBuf.getByte(0));
	}
	
	/**
	 * 创建一个ByteBuf的真实副本,副本与源缓冲区的数据是各自独立的
	 */
	@Test
	public void byteBufCopy(){
		Charset utf8 = Charset.forName("UTF-8");
		ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!!", utf8);
		
		//创建该ByteBuf的从索引0开始到索引15的真实副本
		ByteBuf sliced = byteBuf.copy(0, 15);
		System.out.println(sliced.toString(utf8));
		
		//修改副本指定索引处的数据,源缓冲区并不会受到影响
		sliced.setByte(0, (byte)'J');
		
		System.out.println((char)sliced.getByte(0));
		System.out.println((char)byteBuf.getByte(0));
	}
	
	/**
	 * ByteBuf的set和get方法
	 */
	@Test
	public void byteBufSetGet(){
		//创建一个新的ByteBuf以保存给定字符串的字节
		Charset utf8 = Charset.forName("UTF-8");
		ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!!", utf8);
		System.out.println("readerIndex: " + byteBuf.readerIndex() + ", writerIndex: " + byteBuf.writerIndex());
		
		//打印第一个字符
		System.out.println((char)byteBuf.getByte(0));
		System.out.println("readerIndex: " + byteBuf.readerIndex() + ", writerIndex: " + byteBuf.writerIndex());
		
		//将索引为0处的字符更新为B
		byteBuf.setByte(0, (byte)'B');
		//打印第一个字符
		System.out.println((char)byteBuf.getByte(0));
		
		//get和set操作成功,但readerIndex和writerIndex不会改变
		System.out.println("readerIndex: " + byteBuf.readerIndex() + ", writerIndex: " + byteBuf.writerIndex());
	}
	
	/**
     * ByteBuf 上的 read()和 write()操作
     */
	@Test
    public void byteBufWriteRead() {
    	//创建一个新的ByteBuf以保存给定字符串的字节
		Charset utf8 = Charset.forName("UTF-8");
		ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!!", utf8);
		System.out.println("readerIndex: " + byteBuf.readerIndex() + ", writerIndex: " + byteBuf.writerIndex());
		
		//读取第一个字符
		System.out.println((char)byteBuf.readByte());
		System.out.println("readerIndex: " + byteBuf.readerIndex() + ", writerIndex: " + byteBuf.writerIndex());
		
		//向缓冲区中追加字符串
		byteBuf.writeByte((byte)'?');
		
		//write操作成功,会移动writerIndex
		System.out.println("readerIndex: " + byteBuf.readerIndex() + ", writerIndex: " + byteBuf.writerIndex());
    }
}
