/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.debug;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemInsanityControl extends AOTDItem
{
	// Set the item name
	public ItemInsanityControl()
	{
		super();
		this.setUnlocalizedName("insanityControl");
	}

	// When rightclicking + holding shift, decrease insanity, else increase it
	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (!world.isRemote)
		{
			if (entityPlayer.isSneaking() && !entityPlayer.onGround)
			{
				double newInsanity = entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerInsanity() - 5;
				entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerInsanity(newInsanity);
				LogHelper.info("Insanity Level = " + entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerInsanity());
			}
			else if (!entityPlayer.isSneaking() && !entityPlayer.onGround)
			{
				double newInsanity = entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerInsanity() + 5;
				MathHelper.clamp_double(newInsanity, 0, 100);
				entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerInsanity(newInsanity);
				LogHelper.info("Insanity Level = " + entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerInsanity());
			}
			else if (entityPlayer.isSneaking() && entityPlayer.onGround)
			{
				int newVitae = entityPlayer.getCapability(ModCapabilities.ENTITY_DATA, null).getVitaeLevel() + 5;
				if (entityPlayer.getCapability(ModCapabilities.ENTITY_DATA, null).setVitaeLevel(newVitae))
				{
					if (!entityPlayer.capabilities.isCreativeMode)
					{
						entityPlayer.worldObj.createExplosion(entityPlayer, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ(), 2, true).doExplosionB(true);
						entityPlayer.onKillCommand();
					}
				}
				entityPlayer.getCapability(ModCapabilities.ENTITY_DATA, null).syncVitaeLevel();
				LogHelper.info("Vitae Level = " + entityPlayer.getCapability(ModCapabilities.ENTITY_DATA, null).getVitaeLevel());
			}
			else
			{
				int newVitae = entityPlayer.getCapability(ModCapabilities.ENTITY_DATA, null).getVitaeLevel() - 5;
				if (entityPlayer.getCapability(ModCapabilities.ENTITY_DATA, null).setVitaeLevel(newVitae))
				{
					if (!entityPlayer.capabilities.isCreativeMode)
					{
						entityPlayer.worldObj.createExplosion(entityPlayer, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ(), 2, true).doExplosionB(true);
						entityPlayer.onKillCommand();
					}
				}
				entityPlayer.getCapability(ModCapabilities.ENTITY_DATA, null).syncVitaeLevel();
				LogHelper.info("Vitae Level = " + entityPlayer.getCapability(ModCapabilities.ENTITY_DATA, null).getVitaeLevel());
			}
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncPlayerInsanity();
		}
		return itemStack;
	}
}
