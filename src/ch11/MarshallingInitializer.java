package ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import java.io.Serializable;

/**
 * 
 * <p>Description:JBoss Marshalling</p>
 * @author ZhangShenao
 * @date 2017年10月22日
 */
public class MarshallingInitializer extends ChannelInitializer<Channel>{
	private final MarshallerProvider marshallerProvider;
	private final UnmarshallerProvider unmarshallerProvider;
	
	public MarshallingInitializer(MarshallerProvider marshallerProvider,
			UnmarshallerProvider unmarshallerProvider) {
		this.marshallerProvider = marshallerProvider;
		this.unmarshallerProvider = unmarshallerProvider;
	}


	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		//添加MarshallingDecoder以将ByteBuf转换为POJO
		pipeline.addLast(new MarshallingDecoder(unmarshallerProvider));
		
		//添加UnmarshallingEncoder以将POJO转换为ByteBuf
		pipeline.addLast(new MarshallingEncoder(marshallerProvider));
		
		//添加ObjectHandler以处理普通的实现了Serializable接口的POJO
		pipeline.addLast(new ObjectHandler());
	}
	
	public static final class ObjectHandler extends SimpleChannelInboundHandler<Serializable>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Serializable msg)
				throws Exception {
			//处理序列化的POJO
		}
		
	}

}
