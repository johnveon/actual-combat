package com.libuer.actual.combat.java;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * 
 * by zhoufe 2016年11月10日 下午2:20:58
 **/

public class IntLock implements Runnable
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = Logger.getLogger(IntLock.class);

	public static ReentrantLock lock1 = new ReentrantLock();
	public static ReentrantLock lock2 = new ReentrantLock();
	int lock;
	
	
	public IntLock(int lock)
	{
		this.lock = lock;
	}


	@Override
	public void run()
	{
		try
		{
			if(lock == 1)
			{
					lock1.lockInterruptibly();
					
					Thread.sleep(500);
					
					lock2.lockInterruptibly();
					
					logger.info("线程2中断，线程1继续执行");
			}else
			{
				lock2.lockInterruptibly();
				
				Thread.sleep(500);
				
				lock1.lockInterruptibly();//在申请t1的锁时，线程自己interrupt了，就被这个lockInterruptibly响应
				
				logger.info("线程1中断，线程2继续执行");

			}
		} catch (InterruptedException e)
		{
			logger.error(e.getMessage(), e);
		}finally 
		{
			if(lock1.isHeldByCurrentThread())//当前线程是否拥有lock1锁
				lock1.unlock();
			
			if(lock2.isHeldByCurrentThread())
				lock2.unlock();
			
			logger.info(Thread.currentThread().getName() + ":线程退出");
		}
	}

	
	public static void main(String[] args) throws InterruptedException
	{
		IntLock intLockThread1 = new IntLock(1);
		IntLock intLockThread2 = new IntLock(2);
		
		Thread t1 = new Thread(intLockThread1);
		Thread t2 = new Thread(intLockThread2);
		
		t1.setName("intLockThread1");
		t2.setName("intLockThread2");
		
		t1.start();
		t2.start();
		
		Thread.sleep(2000);
		
		t2.interrupt();
		
		
	}
	
}
