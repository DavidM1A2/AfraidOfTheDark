/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;

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
