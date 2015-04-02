package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.utility.NBTHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemJournal extends ItemBase
{
	public ItemJournal()
	{
		super();
		this.setUnlocalizedName("journal");
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (NBTHelper.getString(itemStack, "owner").equals(""))
		{
			if (HasStartedAOTD.get(entityPlayer))
			{
				LogHelper.info("Called");
				NBTHelper.setString(itemStack, "owner", entityPlayer.getDisplayName());
				entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY,
						(int) entityPlayer.posZ);
			}
			else
			{
				if (world.isRemote)
				{
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_SIGN_ID, world, (int) entityPlayer.posX,
							(int) entityPlayer.posY, (int) entityPlayer.posZ);
				}
			}
		}
		else if (NBTHelper.getString(itemStack, "owner").equals(entityPlayer.getDisplayName()))
		{
			if (world.isRemote)
			{
				entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, world, (int) entityPlayer.posX, (int) entityPlayer.posY,
						(int) entityPlayer.posZ);
			}
		}
		else
		{
			if (world.isRemote)
			{
				entityPlayer.addChatMessage(new ChatComponentText("I cannot comprehend this..."));
			}
		}
		return itemStack;
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		NBTHelper.setString(itemStack, "owner", "no one.");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List information, boolean p_77624_4_)
	{
		information.add("Item soulbound to " + NBTHelper.getString(itemStack, "owner"));
	}
}
