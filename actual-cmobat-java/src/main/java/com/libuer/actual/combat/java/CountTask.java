package com.libuer.actual.combat.java;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import org.slf4j.*;

/**
 * 
 * by zhoufe 2016年11月17日 上午9:51:08
 **/

public class CountTask extends RecursiveTask<Long>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	* Logger for this class
	*/
	private static final Logger logger = LoggerFactory.getLogger(CountTask.class);
	private static final long THRESHOLD = 10000L;
	private static final int TASK_AVG = 100;


	private long start;
	private long end;
	
	public CountTask(long start, long end)
	{
		this.start = start;
		this.end = end;
	}


	@Override
	protected Long compute()
	{
		long sum = 0L;
		
		boolean canCompute = (end - start) < THRESHOLD;
		if(canCompute)
		{
			for (long i = start; i <= end; i++)
			{
				sum += i;
			}
		}else
		{
			long step = (start + end) / TASK_AVG;
			List<CountTask> subTasks = new ArrayList<CountTask>();
			long pos = start;
			for (int i = 0; i < TASK_AVG; i++)
			{
				long lastOne = pos + step;
				if(lastOne > end)
					lastOne = end;
				CountTask subtask = new CountTask(pos, lastOne);
				logger.info(String.format("偏移量到了 %s lastOne %s 步长 %s", pos, lastOne, step));
				
				pos += step + 1;
				subTasks.add(subtask);//收集分好的任务
				subtask.fork();	//每个任务分开
			}
			
			for (CountTask countTask : subTasks)
			{
				sum += countTask.join();//每个任务等待
			}
		}
		
		return sum;
	}
	
	
	public static void main(String[] args)
	{
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		CountTask task = new CountTask(0,200000L);
		ForkJoinTask<Long> result = forkJoinPool.submit(task);//把任务提交给自定义的RecursiveTask，让它分解，等待，返回结果
		try
		{
			long re = result.get();
			logger.info("sum=" + re);
		} catch (InterruptedException | ExecutionException e)
		{
			logger.error(e.getMessage(), e);
		}
	}

}
