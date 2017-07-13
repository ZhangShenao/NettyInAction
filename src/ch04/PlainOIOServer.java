package ch04;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * <p>Description:未使用Netty的阻塞网络编程</p>
 * @author ZhangShenao
 * @date 2017年7月13日
 */
public class PlainOIOServer {
	private ServerSocket serverSocket;
	
	public void serve(int port) throws IOException{
		//将服务器绑定到指定端口
		serverSocket = new ServerSocket(port);
		System.out.println("服务器启动: " + serverSocket.getLocalSocketAddress());
		Thread t = null;
		
		for (;;){
			try {
				//接收客户端连接
				final Socket clientSocket = serverSocket.accept();
				System.err.println("接收到客户端的连接: " + clientSocket);
				
				//创建一个新的线程,处理与客户端的通信
				t = new Thread(){
					public void run() {
						PrintWriter out = null;
						try {
							out = new PrintWriter(clientSocket.getOutputStream(),true);
							
							//将消息发送给客户端
							out.println("Hello Client!!");
						} catch (IOException e) {
							e.printStackTrace();
						}finally {
							if (null != out){
								out.close();
							}
							if (null != clientSocket){
								try {
									clientSocket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					};
				};
				
				//启动线程
				t.start();
			}
			catch (Exception e){
				e.printStackTrace();
				
			}
		}
	}
}
