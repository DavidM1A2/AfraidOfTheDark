package com.DavidM1A2.AfraidOfTheDark.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemCloakOfAgility extends AOTDItem
{
	private static double cooldown = 0;

	public ItemCloakOfAgility()
	{
		super();
		this.setUnlocalizedName("cloakOfAgility");
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
	 */
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (cooldown > 0)
		{
			cooldown = cooldown - 0.5;
		}
	}

	public static void setOnCooldown()
	{
		if (cooldown == 0)
		{
			cooldown = 200;
		}
	}

	public static boolean isOnCooldown()
	{
		return cooldown != 0;
	}

	public static int cooldownRemaining()
	{
		return MathHelper.ceiling_double_int(cooldown) / 20 + 1;
	}
}
