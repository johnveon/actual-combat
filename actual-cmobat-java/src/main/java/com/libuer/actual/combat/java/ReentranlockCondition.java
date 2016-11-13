package com.libuer.actual.combat.java;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * by zhoufe 2016年11月11日 下午5:04:34
 **/

public class ReentranlockCondition implements Runnable
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = LoggerFactory.getLogger(ReentranlockCondition.class);
	
	public static ReentrantLock lock = new ReentrantLock();
	public static Condition condition = lock.newCondition();//这个锁绑定的condition


	@Override
	public void run()
	{
		try
		{
			lock.lock();
			condition.await();//等待，叫醒或者interrupt
			
			logger.info("Thread is going on");
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		} finally
		{
			lock.unlock();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		ReentranlockCondition t1 = new ReentranlockCondition();
		Thread thread = new Thread(t1);
		thread.setName("t1-thread");
		thread.start();
		
		Thread.sleep(2*1000);
		
		lock.lock();
		condition.signal();
		lock.unlock();//必须释放，这个锁的condition.await()在接到singal信号会重新获取锁，之后才会继续执行
		
		
	}


}
