package ch07;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * <p>Description:EventLoop接口</p>
 * @author ZhangShenao
 * @date 2017年9月18日
 */
public class EventLoopExamples {
	/**
	 * 在事件循环中执行任务
	 */
	public void executeTaskInEventLoop(){
		boolean terminated = false;
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		while (!terminated){
			//阻塞,直到有事件已经就绪可被运行
			List<Runnable> tasks = blockUntilEventsReady();
			
			//循环遍历,并处理所有的事件
			for (Runnable task : tasks){
				executorService.execute(task);
			}
		}
	}
	
	public List<Runnable> blockUntilEventsReady(){
		return Collections.<Runnable>singletonList(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
