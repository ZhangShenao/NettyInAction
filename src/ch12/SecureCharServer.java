package ch12;

import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * 
 * <p>Description:  向ChatServer添加加密</p>
 * <p>扩展ChatServer以支持加密</p>
 * @author ZhangShenao
 * @date 2017年10月25日 下午11:14:59
 */
public class SecureCharServer extends ChatServer{
	private final SslContext sslCtx;
	
	public SecureCharServer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	protected ChannelInitializer<Channel> createChildInitializer(ChannelGroup group) {
		//返回SecureChatServerInitializer以启动加密
		return new SecureChatServerInitializer(group, sslCtx);
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		if (args.length < 1){
			System.err.println("Please give port as argument");
            System.exit(1);
		}
		
		int port = ChatServer.DEFAULT_PORT;
		try {
			port = Integer.parseInt(args[0]);
		}catch (Exception e){
			System.err.println("port must be a integer");
            System.exit(1);
		}
		
		try {
			SelfSignedCertificate cert = new SelfSignedCertificate();
			SslContext sslContext = SslContext.newServerContext(cert.certificate(), cert.privateKey());
			final SecureCharServer endpoint = new SecureCharServer(sslContext);
			ChannelFuture future = endpoint.start(new InetSocketAddress(port));
			
			//设置关闭服务器的钩子
			Runtime.getRuntime().addShutdownHook(new Thread(){
				@Override
				public void run() {
					endpoint.close();
				}
			});
			
			future.channel().closeFuture().syncUninterruptibly();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
