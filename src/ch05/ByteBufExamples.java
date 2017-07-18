package ch05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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
}
