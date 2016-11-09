package com.libuer.actual.combat.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * by zhoufe 2016年11月9日 下午11:06:31
 **/

public class GoodSuspend
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = LoggerFactory.getLogger(GoodSuspend.class);

	public static Object u = new Object();
	
	public static class ChangeObjectThread extends Thread
	{
		volatile boolean suspendme = false;
		
		public void suspendMe()
		{
			suspendme = true;
		}
		
		public void resumeMe()
		{
			suspendme = false;
			
			synchronized(this)
			{
				notify();
			}
		}

		@Override
		public void run()
		{
			while(true)
			{
				synchronized(this)
				{
					while (suspendme)
					{
						try
						{
							wait();//让出cpu资源，给readObjectThread线程打印的2秒
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}
				
				synchronized (u)
				{
					logger.info("in ChangeObjectThread");
				}
				
				Thread.yield();
			}
		}
	}
	
	public static class ReadObjectThread extends Thread
	{
		@Override
		public void run()
		{
			while(true)
			{
				synchronized (u)
				{
					logger.info("in ReadObjectThread");
				}
				
				Thread.yield();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		ChangeObjectThread changeObjectThread = new ChangeObjectThread();
		ReadObjectThread readObjectThread = new ReadObjectThread();
		changeObjectThread.setName("changeObjectThread");
		readObjectThread.setName("readObjectThread");
		changeObjectThread.start();
		readObjectThread.start();
		
		Thread.sleep(1000);
		
		changeObjectThread.suspendMe();
		
		logger.info("suspend changeObjectThread 2 sec");
		Thread.sleep(2 * 1000);
		
		logger.info("resume changeObjectThread");
		changeObjectThread.resumeMe();
		
	}
}
