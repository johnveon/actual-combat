package com.libuer.actual.combat.java;


import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * by zhoufe 2016年11月13日 下午3:33:09
 **/

public class CyclicBarrierDemo
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = LoggerFactory.getLogger(CyclicBarrierDemo.class);

	private static class Soldier implements Runnable
	{

		private final CyclicBarrier cyclic;
		private String soldierName;

		public Soldier(CyclicBarrier cyclic, String soldierName)
		{
			this.cyclic = cyclic;
			this.soldierName = soldierName;
		}

		@Override
		public void run()
		{
			try
			{
				//等待所有士兵到齐
				cyclic.await();	
				doWork();
				//等待所有士兵完成工作
				cyclic.await();
			} catch (InterruptedException | BrokenBarrierException e)
			{
				logger.error(e.getMessage(), e);
			}
		}

		private void doWork()
		{
			try
			{
				Thread.sleep(Math.abs(new Random().nextInt()%100000));
			} catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			} 
			logger.info(soldierName + ":任务完成！");
		}
		
	}
	
	private static class BarrierRun implements Runnable
	{
		private boolean flag;
		private int N;

		public BarrierRun(boolean flag, int N)
		{
			this.flag = flag;
			this.N = N;
		}

		@Override
		public void run()//完成10次await()就会调用一次
		{
			if(flag)
			{
				logger.info("司令:[士兵"+N+"个，任务完成！]");
			}else
			{
				logger.info("司令:[士兵"+N+"个，集合完毕！]");
				flag = true;
			}
			
		}
		
	}
	
	public static void main(String[] args)
	{
		//司令命令10个士兵集合，再一起去执行任务，10个士兵都执行完成任务之后，触发完成任务的动作
		final int N = 10;
		Thread[] allSoldier = new Thread[N];
		boolean flag = false;
		CyclicBarrier cyclic = new CyclicBarrier(N, new BarrierRun(flag, N));//BarrierRun完成10次await就执行的动作
		
		logger.info("集合队伍！");
		
		for (int i = 0; i < N; i++)
		{
			allSoldier[i] = new Thread(new Soldier(cyclic, "士兵" + i));
			allSoldier[i].start();
		}

	}

}
