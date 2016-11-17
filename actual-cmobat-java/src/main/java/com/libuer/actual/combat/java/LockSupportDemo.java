package com.libuer.actual.combat.java;

import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * by zhoufe 2016年11月14日 上午11:01:10
 **/

public class LockSupportDemo
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = LoggerFactory.getLogger(LockSupportDemo.class);

	public static Object u = new Object();

	static ChangeObjectThread t1 = new ChangeObjectThread("t1");
	static ChangeObjectThread t2 = new ChangeObjectThread("t2");
	
	public static class ChangeObjectThread extends Thread
	{
		public ChangeObjectThread(String name)
		{
			super.setName(name);
		}

		@Override
		public void run()
		{
			synchronized(u)
				{
					logger.info("in " + getName());
					//WAITING(parking)
					//LockSupport.park();//停在这里，unpark就执行，不管是在park之前还是之后
					LockSupport.park(this);//dump 会出现parking wait for xxx
					if(Thread.interrupted())//park 线程中断了不会抛出异常，但会改变Thread.interrupted状态
					{
						logger.info(getName() + " 被中断了");
					}
				}
			logger.info(getName() + " 执行结束");

		}
		
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		t1.start();
		Thread.sleep(100);
		
		t2.start();
		t1.interrupt();
		//LockSupport.unpark(t1);//许可设置为可用，park就不阻塞了
		LockSupport.unpark(t2);//许可设置为可用
		
		//t1.join();//主线程要等待t1执行完毕，才能结束
		//t2.join();//主线程要等待t2执行完毕，才能结束
		
		
		
		
		

	}

}
