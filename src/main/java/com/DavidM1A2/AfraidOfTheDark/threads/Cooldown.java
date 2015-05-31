package com.DavidM1A2.AfraidOfTheDark.threads;

import com.DavidM1A2.AfraidOfTheDark.item.IHasCooldown;

public class Cooldown<T extends IHasCooldown> extends Thread
{
	private final int COOLDOWN;
	private long launchTime;
	private final T classWithCooldown;

	public Cooldown(final T classWithCooldown)
	{
		this.COOLDOWN = classWithCooldown.getItemCooldownInMillis();
		this.classWithCooldown = classWithCooldown;
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
		classWithCooldown.cooldownCallback();
	}
	
	public boolean onCooldown()
	{
		return this.isAlive();
	}

	public int getSecondsRemaining()
	{
		return (int) ((this.COOLDOWN - (System.currentTimeMillis() - this.launchTime)) / 1000);
	}
}
