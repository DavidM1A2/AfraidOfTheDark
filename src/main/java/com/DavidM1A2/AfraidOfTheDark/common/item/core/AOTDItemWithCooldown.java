/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.core;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class AOTDItemWithCooldown extends AOTDItem implements IHasCooldown
{
	protected double cooldown = 0;
	private long lastUpdate = -1;

	public AOTDItemWithCooldown()
	{
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public boolean showDurabilityBar(ItemStack itemStack)
	{
		return true;
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack
	 *            The current ItemStack
	 * @return 1.0 for 100% 0 for 0%
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return cooldown / this.getItemCooldownInTicks();
	}

	public void setOnCooldown()
	{
		if (cooldown == 0)
		{
			cooldown = this.getItemCooldownInTicks();
		}
	}

	public boolean isOnCooldown()
	{
		return cooldown != 0;
	}

	public int cooldownRemaining()
	{
		return MathHelper.ceiling_double_int(cooldown) / 20 + 1;
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
	 */
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (cooldown > 0)
		{
			if (lastUpdate + 50 <= System.currentTimeMillis())
			{
				lastUpdate = System.currentTimeMillis();
				cooldown = cooldown - 1;
			}
		}
	}

	@Override
	public boolean isDamageable()
	{
		return true;
	}
}
