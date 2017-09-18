package ch07;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.cookie.CookieHeaderNames;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * 
 * <p>Description:任务调度</p>
 * @author ZhangShenao
 * @date 2017年9月18日
 */
public class ScheduleExamples {
	private static final Channel CHANNEL_FORM_SOMEWHERE = new NioSocketChannel();
	
	/**
	 * 使用ScheduledExecutorsService执行调度任务
	 */
	@Test
	public void schedule() throws InterruptedException{
		//创建一个内部具有10个线程池的ScheduledExecutorService
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
		
		//调度任务
		scheduledThreadPool.schedule(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("30 seconds later");
			}
		}, 5, TimeUnit.SECONDS);
		
		//关闭ScheduledExecutorService,释放资源
		scheduledThreadPool.awaitTermination(10, TimeUnit.SECONDS);
	}
	
	/**
	 * 使用EventLoop调度任务
	 */
	@Test
	public void scheduleViaEventLoop(){
		Channel channel = CHANNEL_FORM_SOMEWHERE;
		channel.eventLoop().schedule(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("30 seconds later");
			}
		}, 5, TimeUnit.SECONDS);
	}
	
	/**
	 * 使用EventLoop调度周期性任务
	 */
	public void scheduleFixedViaEventLoop(){
		Channel channel = CHANNEL_FORM_SOMEWHERE;
		channel.eventLoop().scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("30 seconds later");
			}
		}, 0,5, TimeUnit.SECONDS);
	}
	
	/**
	 * 使用ScheduledFuture取消任务
	 */
	public void cancelingTaskUsingScheduledFuture(){
		Channel channel = CHANNEL_FORM_SOMEWHERE;
		ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("30 seconds later");
			}
		}, 0,5, TimeUnit.SECONDS);
		
		boolean mayInterruptIfRunning = false;
		
		//取消任务
		future.cancel(mayInterruptIfRunning);
	}
}
