package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;

public class ItemTelescope extends AOTDItem
{
	public ItemTelescope()
	{
		super();
		this.setUnlocalizedName("telescope");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (world.isRemote)
		{
			if (entityPlayer.getPosition().getY() <= 128)
			{
				entityPlayer.addChatComponentMessage(new ChatComponentText("§oI §ocan't §osee §oanything §othrough §othese §othick §oclouds. §oMaybe §oI §ocould §omove §oto §oa §ohigher §oelevation."));
			}
			else
			{
				entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.TELESCOPE_ID, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
			}
		}

		return itemStack;
	}
}
