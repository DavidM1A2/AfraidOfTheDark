/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class ItemSextant extends AOTDItem
{
	// Quick silver ingot item
	public ItemSextant()
	{
		super();
		this.setUnlocalizedName("sextant");
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (world.isRemote)
		{
			if (HasStartedAOTD.get(entityPlayer) && Research.isResearched(entityPlayer, ResearchTypes.AstronomyI.getPrevious()))
			{
				entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.SEXTANT_ID, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
			}
			else
			{
				entityPlayer.addChatComponentMessage(new ChatComponentText("§oI §ocan't §ounderstand §owhat §othis §othing §odoes."));
			}
		}
		return itemStack;
	}
}
