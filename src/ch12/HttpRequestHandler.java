package ch12;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 
 * <p>Description: 扩展 SimpleChannelInboundHandler 以处理 FullHttpRequest 消息 </p>
 * @author ZhangShenao
 * @date 2017年10月24日 下午10:20:26
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
	private final String wsUrl;			//WebSocket的URL
	private static final File INDEX;	//首页文件
	
	static {
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
		
		//解析文件路径
		try {
			String path = location.toURI() + "index.html";
			path = !path.contains("file:") ? path : path.substring(5);
			INDEX = new File(path);
		}catch (URISyntaxException e) {
            throw new IllegalStateException(
                    "Unable to locate index.html", e);
       }
	}
	
	public HttpRequestHandler(String wsUrl) {
		this.wsUrl = wsUrl;
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)
			throws Exception {
		//如果请求了WebSocket协议升级,则增加引用计数(调用retain方法),并将FullHttpRequest传递给下一个ChannelInboundHandler处理
		//调用retain()方法,是因为SimpleChannelInboundHandler在调用channelRead()后会释放FullHttpRequest
		if (wsUrl.equalsIgnoreCase(request.uri())){
			ctx.fireChannelRead(request.retain());
			return;
		}
		handleNormalHttpRequest(ctx, request);
	}
	
	/**
	 * 处理普通HTTP/S请求
	 */
	private void handleNormalHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception{
		 //处理 100 Continue 请求以符合 HTTP 1.1 规范
        if (HttpUtil.is100ContinueExpected(request)) {
            send100Continue(ctx);
        }
        //读取 index.html
        RandomAccessFile file = new RandomAccessFile(INDEX, "r");
        
        //封装HTTP响应
        HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8");
        
        //如果请求了keep-alive，则添加所需要的 HTTP 头信息
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        }
        
        //将 HttpResponse 写到客户端
        ctx.write(response);
        
        //将 index.html 写到客户端
        if (ctx.pipeline().get(SslHandler.class) == null) {
            ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        } else {
            ctx.write(new ChunkedNioFile(file.getChannel()));
        }
        
        //写 LastHttpContent 并冲刷至客户端
        //LastHttpContent标记响应的结束
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        
        //如果没有请求keep-alive，则在写操作完成后关闭 Channel
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        
        file.close();
	}
	
	 private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

}
