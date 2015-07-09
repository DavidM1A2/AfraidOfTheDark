/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public abstract class ItemResearchScroll extends AOTDItem
{
	protected ResearchTypes myType;

	public ItemResearchScroll()
	{
		super();
		this.setMyType();
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (!world.isRemote)
		{
			if (Research.canResearch(entityPlayer, this.myType))
			{
				itemStack.stackSize = itemStack.stackSize - 1;
				Research.unlockResearchSynced(entityPlayer, this.myType, Side.SERVER, true);
			}
			else if (!Research.isResearched(entityPlayer, this.myType))
			{
				entityPlayer.addChatMessage(new ChatComponentText("I don't understand the material refrenced in this research scroll."));
			}
		}

		return super.onItemRightClick(itemStack, world, entityPlayer);
	}

	public abstract void setMyType();
}
