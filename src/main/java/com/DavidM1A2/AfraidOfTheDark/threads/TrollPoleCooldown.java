package com.DavidM1A2.AfraidOfTheDark.threads;

public class TrollPoleCooldown extends Thread
{
	private final int COOLDOWN;
	private long launchTime;

	public TrollPoleCooldown(final int cooldown)
	{
		this.COOLDOWN = cooldown;
	}

	@Override
	public void run()
	{
		this.launchTime = System.currentTimeMillis();
		try
		{
			Thread.sleep(this.COOLDOWN);
		}
		catch (final InterruptedException e)
		{
		}
	}

	public int getSecondsRemaining()
	{
		return (int) ((this.COOLDOWN - (System.currentTimeMillis() - this.launchTime)) / 1000);
	}
}
