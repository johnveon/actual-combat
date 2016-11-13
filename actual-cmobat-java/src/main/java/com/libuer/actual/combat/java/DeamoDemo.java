package com.libuer.actual.combat.java;

import org.apache.log4j.Logger;

/**
 * 
 * by zhoufe 2016年11月10日 上午10:24:11
 **/

public class DeamoDemo
{
	/**
	* Logger for this class
	*/
	private static final Logger logger = Logger.getLogger(DeamoDemo.class);

	public static class DeamonT extends Thread
	{
		@Override
		public void run()
		{
			while(true)
			{
				logger.info("I am alive");
				
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		Thread t = new DeamonT();
		t.setDaemon(true);//只剩守护线程，程序就会退出，因为用户（工作）线程已经没有了，没东西守护
		//t.setDaemon(false);//说明不是线程，是工作线程，要一直完成工作，直到工作完成
		t.start();
		
		Thread.sleep(2000);
		logger.info("main quit");
		
	}
}
