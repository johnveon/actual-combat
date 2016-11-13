package com.libuer.actual.combat.java;


import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * by zhoufe 2016年11月13日 下午2:55:37
 **/

public class ReadWriteLockDemo
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = LoggerFactory.getLogger(ReadWriteLockDemo.class);

	private static Lock lock = new ReentrantLock();
	private int value;
	
	private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static Lock readLock = readWriteLock.readLock();
	private static Lock writeLock = readWriteLock.writeLock();
	
	
	public Object handleRead(Lock lock) throws InterruptedException
	{
		try
		{
			lock.lock();
			
			Thread.sleep(2000);//读操作的耗时越多，读写锁的优势就越明显
			
			return value;
		} finally
		{
			lock.unlock();
		}
	}
	
	
	public void handleWrite(Lock lock, int index) throws InterruptedException
	{
		try
		{
			lock.lock();
			
			Thread.sleep(1000);
			
			value = index;
		} finally
		{
			lock.unlock();
		}
	}
	

	public static void main(String[] args)
	{
		final ReadWriteLockDemo demo = new ReadWriteLockDemo();
		Runnable readRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					demo.handleRead(readLock);
					//demo.handleRead(lock);
				} catch (Exception e)
				{
					logger.error(e.getMessage(), e);
				}
				
			}
		};
		
		Runnable writeRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					demo.handleWrite(writeLock, new Random().nextInt());
					//demo.handleWrite(lock, new Random().nextInt());
				} catch (Exception e)
				{
					logger.error(e.getMessage(), e);
				}
				
			}
		};
		
		
		for (int i = 0; i < 18; i++)
		{
			new Thread(readRunnable).start();
		}
		
		for (int i = 18; i < 20; i++)
		{
			new Thread(writeRunnable).start();
		}
		
		
		
	}

}
