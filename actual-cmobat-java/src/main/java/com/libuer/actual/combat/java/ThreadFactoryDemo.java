package com.libuer.actual.combat.java;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * by zhoufe 2016年11月16日 上午10:03:50
 **/

public class ThreadFactoryDemo
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = LoggerFactory.getLogger(ThreadFactoryDemo.class);

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
			} catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		
	}

	public static void main(String[] args) throws InterruptedException
	{
		MyTask task = new MyTask();
		
		ExecutorService es = new ThreadPoolExecutor(5, 5, 0L, 
				TimeUnit.MILLISECONDS, 
				new SynchronousQueue<Runnable>(),//直接提交队列
				new ThreadFactory()
				{
					@Override
					public Thread newThread(Runnable r)
					{
						Thread thread = new Thread(r);
						thread.setDaemon(true);//主线程退出，线程也退出了
						thread.setName("es-thread-" + UUID.randomUUID().toString());
						logger.info("create:"+thread);
						return thread;
					}
				});
		
		for (int i = 0; i < 5; i++)
		{
			es.submit(task);
		}
		
		Thread.sleep(2000);//没有这句主线程退出，在先，还没等子线程执行程序就退出了
	}

}
