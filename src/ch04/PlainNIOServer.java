package ch04;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * <p>Description:未使用Netty的异步网络编程</p>
 * @author ZhangShenao
 * @date 2017年7月13日
 */
public class PlainNIOServer {
	public void serve(int port) {
		ServerSocketChannel serverSocketChannel = null;
		try {
			//开启ServerSocketChannel
			serverSocketChannel = ServerSocketChannel.open();
			
			//将ServerSocketChannle设置为非阻塞模式
			serverSocketChannel.configureBlocking(false);
			
			//将服务器绑定到指定的端口
			ServerSocket serverSocket = serverSocketChannel.socket();
			serverSocket.bind(new InetSocketAddress(port));
			
			//开启Selector
			Selector selector = Selector.open();
			
			//将ServerSocketChannel注册到Selector上以接收客户端连接
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			//创建ByteBuffer与客户端通信
			ByteBuffer msg = ByteBuffer.wrap("Hello Client!!".getBytes("UTF-8"));
			
			for (;;){
				//等待需要处理的新事件,将一直阻塞知道下一个事件到来
				selector.select();
				
				//获取所有接收事件的SelectionKey实例
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> keys = selectedKeys.iterator();
				while (keys.hasNext()){
					SelectionKey selectionKey = keys.next();
					keys.remove();
					
					try {
						//如果该事件是一个连接就绪事件
						if (selectionKey.isAcceptable()){
							//获取服务器的ServerSocketChannel
							ServerSocketChannel server = (ServerSocketChannel)selectionKey.channel();
							
							//接收客户端连接,返回与客户端通信的SocketChannel
							SocketChannel client = server.accept();
							
							//将SocketChannel设置为非阻塞模式
							client.configureBlocking(false);
							
							//将客户端SocketChannel注册到Selector上,注册读就绪和写就绪事件并将ByteBuffer作为附件
							client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE,msg);
							
							System.err.println("接收到客户端连接: " + client);
							
						}
						
						//如果该事件是一个写就绪事件
						if (selectionKey.isWritable()){
							//获取与客户端通信的SocketChannel
							SocketChannel client = (SocketChannel) selectionKey.channel();
							
							//获取附件
							ByteBuffer attachment = (ByteBuffer) selectionKey.attachment();
							
							//将数据写到已连接的客户端
							while (attachment.hasRemaining()){
								client.write(attachment);
							}
							
							//写入客户端结束,关闭连接
							client.close();
						}
					}catch (Exception e){
						e.printStackTrace();
						
						//抛出异常时,取消该事件,并关闭该事件所属的Channel
						selectionKey.cancel();
						selectionKey.channel().close();
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (null != serverSocketChannel){
				try {
					serverSocketChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
