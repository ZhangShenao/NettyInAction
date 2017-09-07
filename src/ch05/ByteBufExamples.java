package ch05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.collection.ShortObjectMap;

import java.nio.ByteBuffer;

/**
 * 
 * <p>Description:ByteBuf的使用模式</p>
 * @author ZhangShenao
 * @date 2017年7月18日
 */
@SuppressWarnings("unused")
public class ByteBufExamples {
	/**
	 * 一个ByteBuf实例
	 */
	private final static ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);
	
	public static void main(String[] args) {
		byteBufComposite();
	}
	
	/**
	 * 处理数组的业务逻辑方法
	 * @param array 目标数组
	 * @param offset 偏移量
	 * @param len 长度
	 */
	private static void handleArray(byte[] array, int offset, int len) {
		
	}
	 
	/**
	 * 支撑数组
	 * 将数据存储在JVM的堆空间中,可以在没用使用池化的情况下快速的分配和释放
	 */
	private static void heapBuffer(){
		ByteBuf heapBuf = BYTE_BUF_FROM_SOMEWHERE;
		
		//检查ByteBuf是否有一个支撑数组
		if (heapBuf.hasArray()){
			//如果有支撑数组,则获取对该数组的引用
			byte[] array = heapBuf.array();
			
			//计算第一个字节的偏移量
			int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
			
			//获取可读的字节数
			int length = heapBuf.readableBytes();
			
			//使用数组、偏移量和长度作为参数,调用自己的业务方法
			handleArray(array, offset, length);
		}
	}
	
	/**
	 * 访问直接缓冲区的数据
	 */
	private static void directBuffer(){
		ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE;
		
		//检查ByteBuf是否由一个数组支撑。如果不是,则是一个直接缓冲区
		if (!directBuf.hasArray()){
			//获取可读字节数
			int length = directBuf.readableBytes();
			
			//分配一个新的字节数组来保存该长度的字节数据
			byte[] array = new byte[length];
			
			//将ByteBuf中的字节复制到数组中
			directBuf.getBytes(directBuf.readerIndex(), array);
			
			//使用数组、偏移量和长度作为参数,调用自己的业务方法
			handleArray(array, 0, length);
		}
	}
	
	/**
	 * 使用JDK提供的ByteBuffer的复合缓冲区模式
	 * @param header 消息头
	 * @param body 消息主体
	 */
	public static void byteBufferComposite(ByteBuffer header, ByteBuffer body) {
		//创建一个ByteBuffer数组保存消息头和消息体
        ByteBuffer[] message =  new ByteBuffer[]{ header, body };
        
        //创建另一个ByteBuffer保存这些消息的副本,并将他们合并
        ByteBuffer message2 =
                ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }
	
	/**
	 * 使用CompositeByteBuf的复合缓冲区模式
	 */
	public static void byteBufComposite(){
		//创建CompositeByteBuf复合缓冲区对象
		CompositeByteBuf compositeBuffer = Unpooled.compositeBuffer();
		
		//将ByteBuf实例追加到CompositeByteBuf
		ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE;
		ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE;
		compositeBuffer.addComponents(headerBuf,bodyBuf);
		
		//删除第一个ByteBuf
		compositeBuffer.removeComponent(0);
		
		//遍历CompositeByteBuf组件内的所有ByteBuf实例
		for (ByteBuf byteBuf : compositeBuffer){
			System.out.println(byteBuf);
		}
	}
	
	/**
	 * 访问CompositeByteBuf中的数据
	 */
	public static void byteBufCompositeArray() {
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        
        //获得可读字节数
        int length = compBuf.readableBytes();
        
        //分配一个具有可读字节数长度的新数组
        byte[] array = new byte[length];
        
        //将字节读到该数组中
        compBuf.getBytes(compBuf.readerIndex(), array);
        
        //使用偏移量和长度作为参数使用该数组
        handleArray(array, 0, array.length);
    }
}

