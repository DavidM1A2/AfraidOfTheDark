package com.DavidM1A2.AfraidOfTheDark.item;

import com.DavidM1A2.AfraidOfTheDark.threads.Cooldown;

public interface IHasCooldown 
{
	public void cooldownCallback();
	public int getItemCooldownInMillis();
}
