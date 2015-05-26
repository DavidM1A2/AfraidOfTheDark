package com.DavidM1A2.AfraidOfTheDark.threads;

public class TrollPoleCooldown extends Thread
{
	private final int COOLDOWN;
	private long launchTime;
	
	public TrollPoleCooldown(int cooldown)
	{
		this.COOLDOWN = cooldown;
	}
	
	@Override
	public void run()
	{
		this.launchTime = System.currentTimeMillis();
		try 
		{
			Thread.sleep(COOLDOWN);
		} 
		catch (InterruptedException e) 
		{
		}
	}
	
	public int getSecondsRemaining()
	{
		return (int) ((COOLDOWN - (System.currentTimeMillis() - launchTime)) / 1000);
	}
}
