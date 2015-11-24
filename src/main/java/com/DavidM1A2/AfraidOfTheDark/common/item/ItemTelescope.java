/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemTelescope extends AOTDItem
{
	public ItemTelescope()
	{
		super();
		this.setUnlocalizedName("telescope");
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (world.isRemote)
		{
			if (AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.AstronomyI.getPrevious()))
			{
				if (entityPlayer.getPosition().getY() <= 128)
				{
					entityPlayer.addChatComponentMessage(new ChatComponentText("I can't see anything through these thick clouds. Maybe I could move to a higher elevation."));
				}
				else
				{
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.TELESCOPE_ID, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());

					if (AOTDPlayerData.get(entityPlayer).canResearch(ResearchTypes.AstronomyI))
					{
						AOTDPlayerData.get(entityPlayer).unlockResearch(ResearchTypes.AstronomyI, true);
					}
				}
			}
			else
			{
				entityPlayer.addChatComponentMessage(new ChatComponentText("I can't understand what this thing does."));
			}
		}

		return itemStack;
	}
}
