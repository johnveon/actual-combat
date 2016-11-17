package com.libuer.actual.combat.java;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.*;

/**
 * 
 * by zhoufe 2016年11月16日 上午10:53:45
 **/

public class DivTask implements Runnable
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = LoggerFactory.getLogger(DivTask.class);
	private int a,b;

	public DivTask(int a, int b)
	{
		this.a = a;
		this.b = b;
	}


	@Override
	public void run()
	{
		double re = a/b;
		logger.info("" + re);
	}
	
	
	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		ThreadPoolExecutor pools = new ThreadPoolExecutor(
				0,Integer.MAX_VALUE, 0L, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>());
		
		for (int i = 0; i < 5; i++)
		{
			pools.submit(new DivTask(100, i));//期望提交5个任务，打印5个结果。但是打印4个结果，还没有异常
			//解决方法:
//			Future re = pools.submit(new DivTask(100, i));//1.期望提交5个任务，打印5个结果。但是打印4个结果，有部分异常
//			re.get();
			
//			pools.execute(new DivTask(100, i));////2.期望提交5个任务，打印5个结果。但是打印4个结果，有部分异常
			
			//3.自己重写线程池TraceThreadPoolExecutor
			
		}
	}
}
//~
//100.0
//50.0
//25.0
//33.0
