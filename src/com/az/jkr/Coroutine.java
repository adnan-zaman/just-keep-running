package com.az.jkr;

import java.lang.Thread.State;


/**
 * Takes some functionality and calls
 * it after a certain amount of time
 * on another thread.
 *
 */
public class Coroutine {

	private long waitTime;
	private Runnable runnable;
	private Thread thread;
	
	/** A thread that waits
	 * an amount of time before running
	 */
	private class CoroutineThread extends Thread
	{
		public CoroutineThread(Runnable r)
		{
			super(r);
		}
		
		public void run()
		{
			try
			{
				Thread.sleep(waitTime);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			super.run();
		}
	}
	
	
	public Coroutine(long wait, Runnable r)
	{
		waitTime = wait;
		runnable = r;
		thread = new CoroutineThread(r);
	}
	
	/**
	 * Start the coroutine
	 */
	public void start()
	{
		//already running
		if (thread.isAlive())
			return;
		
		//has finished, get a new thread
		if (thread.getState() == State.TERMINATED)
			thread = new CoroutineThread(runnable);
	
		
		thread.start();
	}
	
	public long getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
}
