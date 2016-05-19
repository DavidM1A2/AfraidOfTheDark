/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemJournal extends AOTDItem
{
	// Set the name and stack size to 1
	public ItemJournal()
	{
		super();
		this.setUnlocalizedName("journal");
		this.setMaxStackSize(1);
	}

	// If you right click with a journal
	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (itemStack.getItemDamage() == 0)
		{
			// If the journal has no owner
			if (NBTHelper.getString(itemStack, "owner").equals(""))
			{
				// If the player has started AOTD, set the NBT tag and open the
				// journal
				if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD())
				{
					NBTHelper.setString(itemStack, "owner", entityPlayer.getDisplayName().getUnformattedText());
					if (world.isRemote)
						entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
				}
				else
				{
					// else open the signup page
					if (world.isRemote)
						entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_SIGN_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
				}
			}
			// If the owner is the current entityPlayer then open the journal
			else if (NBTHelper.getString(itemStack, "owner").equals(entityPlayer.getDisplayName().getUnformattedText()))
			{
				if (world.isRemote)
					if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD())
						entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
					else
					{
						entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_SIGN_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
						NBTHelper.setString(itemStack, "owner", "");
					}
			}
			// Else this is someone else's journal so you cannot comprehend it
			else
			{
				if (!world.isRemote)
					entityPlayer.addChatMessage(new ChatComponentText("I cannot comprehend this..."));
			}
		}
		else if (itemStack.getItemDamage() == 1)
		{
			if (entityPlayer.capabilities.isCreativeMode)
			{
				if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD())
				{
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_CHEAT_SHEET, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
				}
				else
				{
					if (!world.isRemote)
					{
						entityPlayer.addChatMessage(new ChatComponentText("You will need to sign a standard journal first."));
					}
				}
			}
			else
			{
				if (!world.isRemote)
				{
					entityPlayer.addChatMessage(new ChatComponentText("You must be in creative mode to use the cheat sheet."));
				}
			}
		}
		return itemStack;
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 * 
	 * @param subItems
	 *            The List of sub-items. This is a List of ItemStacks.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List subItems)
	{
		subItems.add(new ItemStack(item, 1, 0));
		subItems.add(new ItemStack(item, 1, 1));
	}

	// The journal shows who it is soulbound to
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List information, final boolean p_77624_4_)
	{
		if (itemStack.getItemDamage() == 0)
		{
			information.add("Item soulbound to " + NBTHelper.getString(itemStack, "owner"));
		}
		else if (itemStack.getItemDamage() == 1)
		{
			information.add("Cheat Sheet");
		}
	}
}
