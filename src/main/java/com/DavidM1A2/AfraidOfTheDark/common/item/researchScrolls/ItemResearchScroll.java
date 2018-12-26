/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls;

import java.util.List;

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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand hand)
	{
		ItemStack itemStack = entityPlayer.getHeldItem(hand);
		if (!world.isRemote)
		{
			if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(this.myType))
			{
				if (itemStack.getMetadata() == 0)
				{
					// Reduce stack size by 1
					itemStack.shrink(1);
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(this.myType, true);
				}
				else
				{
					entityPlayer.sendMessage(new TextComponentString("This research scroll is not complete yet."));
				}
			}
			else if (!entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(this.myType))
			{
				entityPlayer.sendMessage(new TextComponentString("I don't understand the material refrenced in this research scroll."));
			}
		}

		return ActionResult.<ItemStack> newResult(EnumActionResult.SUCCESS, itemStack);
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
