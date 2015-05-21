package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemIgneousGem extends AOTDItem
{
	public ItemIgneousGem()
	{
		super();
		this.setUnlocalizedName("igneousGem");
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	{
		entity.setFire(3);
	}
}
