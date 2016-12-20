package com.seentao.stpedu.common.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;


/**
 * 目前用于
 * 1。竞猜公布结果延迟计算奖金
 * @author 	lw
 * @date	2016年7月16日  下午5:20:31
 *
 */
@Component
public class PulblicRunnableThreadPool {

	/**
	 * 线程池
	 * 
	 */
	private static final ExecutorService executor = newCachedThreadPool();
	
	/**
	 * 线程池初始化
	 * 创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE
	 * 来了任务就创建线程运行，当线程空闲超过30秒，就销毁线程.
	 * @param nThreads
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午10:52:11
	 */
	public static ExecutorService newCachedThreadPool(){
		ThreadPoolExecutor pool =  new ThreadPoolExecutor(	0						//核心线程池链接数量	
										, Integer.MAX_VALUE							//最大线程池链接数量
										, 30L										//线程池大于 核心线程池 的时候 没有执行任务最多保持时间 （到时终止）
										, TimeUnit.SECONDS							//保持线程 时间 单位 。 例如此刻是30秒（单位秒）
										, new SynchronousQueue<Runnable>());		//阻塞列队，用于等待线程
		pool.allowCoreThreadTimeOut(true);											//核心线程空闲时 自动销毁
		return pool;
	}
	
	
	/**
	 * 线程池添加任务
	 * @param task
	 * @author 			lw
	 * @date			2016年7月4日  下午11:03:55
	 */
	public static void submit(Runnable task){
		executor.execute(task);
	}
	
}
