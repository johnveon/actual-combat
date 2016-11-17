package com.libuer.actual.combat.java;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * by zhoufe 2016年11月16日 上午11:08:22
 **/

public class TraceThreadpoolExecutor extends ThreadPoolExecutor
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = LoggerFactory.getLogger(TraceThreadpoolExecutor.class);

	public TraceThreadpoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	

	@Override
	public void execute(Runnable command)
	{
		super.execute(wrap(command, clientTrace(), Thread.currentThread().getName()));
	}



	private Runnable wrap(final Runnable command,final Exception clientTrace,final String name)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					command.run();
				} catch (Exception e)
				{
					clientTrace.printStackTrace();
					throw e;
					//logger.error(e.getMessage(), e);
				}
				
			}
		};
	}


	private Exception clientTrace()
	{
		return new Exception("client stack trace");
	}


	@Override
	public Future<?> submit(Runnable task)
	{
		return super.submit(wrap(task, clientTrace(), Thread.currentThread().getName()));
	}



	public static void main(String[] args)
	{
		TraceThreadpoolExecutor pools = new TraceThreadpoolExecutor(
				0,Integer.MAX_VALUE, 0L, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>());
		
		for (int i = 0; i < 5; i++)
		{
			pools.execute(new DivTask(100, i));
		}
	}

}
