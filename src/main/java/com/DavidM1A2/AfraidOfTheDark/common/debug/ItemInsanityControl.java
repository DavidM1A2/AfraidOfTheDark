/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.debug;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDEntityData;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;
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
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer myPlayer)
	{
		if (!world.isRemote)
		{
			if (myPlayer.isSneaking() && !myPlayer.onGround)
			{
				double newInsanity = AOTDPlayerData.get(myPlayer).getPlayerInsanity() - 5;
				AOTDPlayerData.get(myPlayer).setPlayerInsanity(newInsanity);
				LogHelper.info("Insanity Level = " + AOTDPlayerData.get(myPlayer).getPlayerInsanity());
			}
			else if (!myPlayer.isSneaking() && !myPlayer.onGround)
			{
				double newInsanity = AOTDPlayerData.get(myPlayer).getPlayerInsanity() + 5;
				MathHelper.clamp_double(newInsanity, 0, 100);
				AOTDPlayerData.get(myPlayer).setPlayerInsanity(newInsanity);
				LogHelper.info("Insanity Level = " + AOTDPlayerData.get(myPlayer).getPlayerInsanity());
			}
			else if (myPlayer.isSneaking() && myPlayer.onGround)
			{
				int newVitae = AOTDEntityData.get(myPlayer).getVitaeLevel() + 5;
				if (AOTDEntityData.get(myPlayer).setVitaeLevel(newVitae))
				{
					if (!myPlayer.capabilities.isCreativeMode)
					{
						myPlayer.worldObj.createExplosion(myPlayer, myPlayer.getPosition().getX(), myPlayer.getPosition().getY(), myPlayer.getPosition().getZ(), 2, true).doExplosionB(true);
						myPlayer.onKillCommand();
					}
				}
				AOTDEntityData.get(myPlayer).syncVitaeLevel();
				LogHelper.info("Vitae Level = " + AOTDEntityData.get(myPlayer).getVitaeLevel());
			}
			else
			{
				int newVitae = AOTDEntityData.get(myPlayer).getVitaeLevel() - 5;
				if (AOTDEntityData.get(myPlayer).setVitaeLevel(newVitae))
				{
					if (!myPlayer.capabilities.isCreativeMode)
					{
						myPlayer.worldObj.createExplosion(myPlayer, myPlayer.getPosition().getX(), myPlayer.getPosition().getY(), myPlayer.getPosition().getZ(), 2, true).doExplosionB(true);
						myPlayer.onKillCommand();
					}
				}
				AOTDEntityData.get(myPlayer).syncVitaeLevel();
				LogHelper.info("Vitae Level = " + AOTDEntityData.get(myPlayer).getVitaeLevel());
			}
			AOTDPlayerData.get(myPlayer).syncPlayerInsanity();
		}
		return itemStack;
	}
}
