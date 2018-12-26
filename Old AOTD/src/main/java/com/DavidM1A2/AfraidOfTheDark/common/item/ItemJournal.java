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

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemJournal extends AOTDItem
{
	// Set the name and stack size to 1
	public ItemJournal()
	{
		super();
		this.setUnlocalizedName("journal");
		this.setRegistryName("journal");
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand hand)
	{
		ItemStack itemStack = entityPlayer.getHeldItem(hand);
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
						entityPlayer.openGui(AfraidOfTheDark.INSTANCE, GuiHandler.BLOOD_STAINED_JOURNAL_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
				}
				else
				{
					// else open the signup page
					if (world.isRemote)
						entityPlayer.openGui(AfraidOfTheDark.INSTANCE, GuiHandler.BLOOD_STAINED_JOURNAL_SIGN_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
				}
			}
			// If the owner is the current entityPlayer then open the journal
			else if (NBTHelper.getString(itemStack, "owner").equals(entityPlayer.getDisplayName().getUnformattedText()))
			{
				if (world.isRemote)
					if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD())
						entityPlayer.openGui(AfraidOfTheDark.INSTANCE, GuiHandler.BLOOD_STAINED_JOURNAL_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
					else
					{
						entityPlayer.openGui(AfraidOfTheDark.INSTANCE, GuiHandler.BLOOD_STAINED_JOURNAL_SIGN_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
						NBTHelper.setString(itemStack, "owner", "");
					}
			}
			// Else this is someone else's journal so you cannot comprehend it
			else
			{
				if (!world.isRemote)
					entityPlayer.sendMessage(new TextComponentString("I cannot comprehend this..."));
			}
		}
		else if (itemStack.getItemDamage() == 1)
		{
			if (entityPlayer.capabilities.isCreativeMode)
			{
				if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD())
				{
					entityPlayer.openGui(AfraidOfTheDark.INSTANCE, GuiHandler.BLOOD_STAINED_JOURNAL_CHEAT_SHEET, world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
				}
				else
				{
					if (!world.isRemote)
					{
						entityPlayer.sendMessage(new TextComponentString("You will need to sign a standard journal first."));
					}
				}
			}
			else
			{
				if (!world.isRemote)
				{
					entityPlayer.sendMessage(new TextComponentString("You must be in creative mode to use the cheat sheet."));
				}
			}
		}
		return ActionResult.<ItemStack> newResult(EnumActionResult.SUCCESS, itemStack);
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 * 
	 * @param items
	 *            The List of sub-items. This is a List of ItemStacks.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}

	// The journal shows who it is soulbound to
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (stack.getItemDamage() == 0)
		{
			tooltip.add("Item soulbound to " + NBTHelper.getString(stack, "owner"));
		}
		else if (stack.getItemDamage() == 1)
		{
			tooltip.add("Cheat Sheet");
		}
	}
}
