/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemTelescope extends AOTDItem
{
	public ItemTelescope()
	{
		super();
		this.setUnlocalizedName("telescope");
		this.setRegistryName("telescope");
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer entityPlayer, EnumHand hand)
	{
		ItemStack itemStack = entityPlayer.getHeldItem(hand);
		if (world.isRemote)
		{
			if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.AstronomyI.getPrevious()))
			{
				if (entityPlayer.getPosition().getY() <= 128)
				{
					entityPlayer.sendMessage(new TextComponentString("I can't see anything through these thick clouds. Maybe I could move to a higher elevation."));
				}
				else
				{
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.TELESCOPE_ID, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());

					if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.AstronomyI))
					{
						entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.AstronomyI, true);
					}
				}
			}
			else
			{
				entityPlayer.sendMessage(new TextComponentString("I can't understand what this thing does."));
			}
		}

		return ActionResult.<ItemStack> newResult(EnumActionResult.SUCCESS, itemStack);
	}
}
