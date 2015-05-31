/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
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
	public void onUpdate(final ItemStack itemstack, final World world, final Entity entity, final int i, final boolean flag)
	{
		entity.setFire(3);
	}
}
