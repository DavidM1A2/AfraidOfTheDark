/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.utility.NBTHelper;

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
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		// If the journal has no owner
		if (NBTHelper.getString(itemStack, "owner").equals(""))
		{
			// If the player has started AOTD, set the NBT tag and open the journal
			if (HasStartedAOTD.get(entityPlayer))
			{
				NBTHelper.setString(itemStack, "owner", entityPlayer.getDisplayName().getUnformattedText());
				if (world.isRemote)
				{
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
				}
			}
			else
			{
				// else open the signup page
				if (world.isRemote)
				{
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_SIGN_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
				}
			}
		}
		// If the owner is the current entityPlayer then open the journal
		else if (NBTHelper.getString(itemStack, "owner").equals(entityPlayer.getDisplayName().getUnformattedText()))
		{
			if (world.isRemote)
			{
				entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
			}
		}
		// Else this is someone else's journal so you cannot comprehend it
		else
		{
			if (world.isRemote)
			{
				entityPlayer.addChatMessage(new ChatComponentText("I cannot comprehend this..."));
			}
		}
		return itemStack;
	}

	// When created a journal has no owner
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		NBTHelper.setString(itemStack, "owner", "no one.");
	}

	// The journal shows who it is soulbound to
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List information, boolean p_77624_4_)
	{
		information.add("Item soulbound to " + NBTHelper.getString(itemStack, "owner"));
	}
}
