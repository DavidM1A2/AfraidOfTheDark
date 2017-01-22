/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
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

public class ItemSextant extends AOTDItem
{
	// Quick silver ingot item
	public ItemSextant()
	{
		super();
		this.setUnlocalizedName("sextant");
		this.setRegistryName("sextant");
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer entityPlayer, EnumHand hand)
	{
		ItemStack itemStack = entityPlayer.getHeldItem(hand);
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD() && entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.AstronomyI.getPrevious()))
		{
			entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.SEXTANT_ID, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
		}
		else
		{
			if (world.isRemote)
			{
				entityPlayer.addChatMessage(new TextComponentString("I can't understand what this thing does."));
			}
		}
		return ActionResult.<ItemStack> newResult(EnumActionResult.SUCCESS, itemStack);
	}
}
