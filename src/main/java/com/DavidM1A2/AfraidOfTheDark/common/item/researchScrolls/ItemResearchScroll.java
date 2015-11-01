/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemResearchScroll extends AOTDItem
{
	protected ResearchTypes myType;

	public ItemResearchScroll()
	{
		super();
		this.setMyType();
		this.setMaxStackSize(1);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (!world.isRemote)
		{
			if (AOTDPlayerData.get(entityPlayer).canResearch(this.myType))
			{
				if (itemStack.getMetadata() == 0)
				{
					itemStack.stackSize = itemStack.stackSize - 1;
					AOTDPlayerData.get(entityPlayer).unlockResearch(this.myType, true);
				}
				else
				{
					entityPlayer.addChatMessage(new ChatComponentText("This research scroll is not complete yet."));
				}
			}
			else if (!AOTDPlayerData.get(entityPlayer).isResearched(this.myType))
			{
				entityPlayer.addChatMessage(new ChatComponentText("I don't understand the material refrenced in this research scroll."));
			}
		}

		return super.onItemRightClick(itemStack, world, entityPlayer);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 * 
	 * @param tooltip
	 *            All lines to display in the Item's tooltip. This is a List of Strings.
	 * @param advanced
	 *            Whether the setting "Advanced tooltips" is enabled
	 */
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List tooltip, boolean advanced)
	{
		if (itemStack.getMetadata() == 0)
		{
			tooltip.add("Scroll is complete.");
		}
		else
		{
			tooltip.add("Part " + itemStack.getMetadata() + " out of " + this.numberOfScrollsToMakeCompleteResearch() + " of the research " + this.myType.formattedString() + ".");
		}
	}

	public abstract void setMyType();

	public int numberOfScrollsToMakeCompleteResearch()
	{
		return 0;
	}
}
