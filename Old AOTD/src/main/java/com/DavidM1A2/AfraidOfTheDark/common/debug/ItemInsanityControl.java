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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemInsanityControl extends AOTDItem
{
	// Set the item name
	public ItemInsanityControl()
	{
		super();
		this.setUnlocalizedName("insanity_control");
		this.setRegistryName("insanity_control");
	}

	// When rightclicking + holding shift, decrease insanity, else increase it
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand hand)
	{
		ItemStack itemStack = entityPlayer.getHeldItemMainhand();
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
				MathHelper.clamp(newInsanity, 0, 100);
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
						entityPlayer.world.createExplosion(entityPlayer, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ(), 2, true).doExplosionB(true);
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
						entityPlayer.world.createExplosion(entityPlayer, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ(), 2, true).doExplosionB(true);
						entityPlayer.onKillCommand();
					}
				}
				entityPlayer.getCapability(ModCapabilities.ENTITY_DATA, null).syncVitaeLevel();
				LogHelper.info("Vitae Level = " + entityPlayer.getCapability(ModCapabilities.ENTITY_DATA, null).getVitaeLevel());
			}
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncPlayerInsanity();
		}

		/*
		    Success: Basically what return true used to be. The call has succeeded in doing what was needed and should stop here.
		    Pass: The call succeeded, but more calls can be made farther down the call stack.
		    Fail: Basically what return false used to be. The call has failed to do what was intended and should stop here.
		*/
		return ActionResult.<ItemStack>newResult(EnumActionResult.SUCCESS, itemStack);
	}
}
