/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;

public class ItemTelescope extends AOTDItem
{
	public ItemTelescope()
	{
		super();
		this.setUnlocalizedName("telescope");
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (world.isRemote)
		{
			if (HasStartedAOTD.get(entityPlayer) && LoadResearchData.isResearched(entityPlayer, ResearchTypes.Astronomy1.getPrevious()))
			{
				if (entityPlayer.getPosition().getY() <= 128)
				{
					entityPlayer.addChatComponentMessage(new ChatComponentText("§oI §ocan't §osee §oanything §othrough §othese §othick §oclouds. §oMaybe §oI §ocould §omove §oto §oa §ohigher §oelevation."));
				}
				else
				{
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.TELESCOPE_ID, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());

					if (!LoadResearchData.isResearched(entityPlayer, ResearchTypes.Astronomy1))
					{
						LoadResearchData.unlockResearchSynced(entityPlayer, ResearchTypes.Astronomy1, Side.CLIENT, true);
					}
				}
			}
			else
			{
				entityPlayer.addChatComponentMessage(new ChatComponentText("§oI §ocan't §ounderstand §owhat §othis §othing §odoes."));
			}
		}

		return itemStack;
	}
}
