package com.libuer.actual.combat.java;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * by zhoufe 2016年11月16日 上午12:33:05
 **/

public class RejectThreadPoolDemo
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = LoggerFactory.getLogger(RejectThreadPoolDemo.class);

	public static class MyTask implements Runnable
	{

		@Override
		public void run()
		{
			logger.info(String.format("%s:Thread ID:%s", 
					System.currentTimeMillis(), Thread.currentThread().getId()));
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		
	}

	public static void main(String[] args) throws InterruptedException
	{
		MyTask task = new MyTask();
		ExecutorService es = new ThreadPoolExecutor(5, 5, 0L,//当线程池中的线程数量>coreSize，多余的空闲线程存活的时间
					TimeUnit.MILLISECONDS, 
					new LinkedBlockingQueue<Runnable>(10), 
					new RejectedExecutionHandler()//任务提交是检查正在工作的线程大于核心线程数（wc>coreSize），
					//任务被提交到等待队列,等待队列已经满了，提交失败，提交到线程池，大于最大线程数（wc>maxSize），就会执行拒绝策略
					//小于就会创建新的线程执行任务，直到maxSize为止	
					{							 
						@Override
						public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
						{
							logger.info(r.toString() + " id discard");
						}
					});
		
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		{
			es.submit(task);
			Thread.sleep(10);
		}
	}

}
