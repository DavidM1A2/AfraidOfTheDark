/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */

package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOtherworldlyPrism extends AOTDItem
{
	private static final String OWNER = "owner";

	// Quick silver ingot item
	public ItemOtherworldlyPrism()
	{
		super();
		this.setUnlocalizedName("otherworldlyPrism");
		this.maxStackSize = 1;
	}

	public void setOwner(EntityPlayer entityPlayer, ItemStack itemStack)
	{
		NBTHelper.setString(itemStack, OWNER, entityPlayer.getDisplayName().getUnformattedText());
	}

	private boolean ownsItem(EntityPlayer entityPlayer, ItemStack itemStack)
	{
		return NBTHelper.getString(itemStack, OWNER).equals(entityPlayer.getDisplayName().getUnformattedText());
	}

	// If you right click with a journal
	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (!ownsItem(entityPlayer, itemStack))
		{
			entityPlayer.addChatMessage(new ChatComponentText("I do not own this item and cannot use it."));
		}
		else
		{
			entityPlayer.addChatMessage(new ChatComponentText("Congratulations " + entityPlayer.getDisplayName().getUnformattedText() + "! You have reached the end of AOTD so far. Please stay tuned for the next update which will add spell crafting in which this item will become very important!"));
		}

		return itemStack;
	}

	// The journal shows who it is soulbound to
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List information, final boolean p_77624_4_)
	{
		information.add("Item soulbound to " + NBTHelper.getString(itemStack, OWNER));
	}
}
