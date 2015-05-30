package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;

public class ItemSextant extends AOTDItem
{
	// Quick silver ingot item
	public ItemSextant()
	{
		super();
		this.setUnlocalizedName("sextant");
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (world.isRemote)
		{
			if (HasStartedAOTD.get(entityPlayer) && LoadResearchData.isResearched(entityPlayer, ResearchTypes.Astronomy1.getPrevious()))
			{
				entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.SEXTANT_ID, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
			}
			else
			{
				entityPlayer.addChatComponentMessage(new ChatComponentText("§oI §ocan't §ounderstand §owhat §othis §othing §odoes."));
			}
		}
		return itemStack;
	}
}
