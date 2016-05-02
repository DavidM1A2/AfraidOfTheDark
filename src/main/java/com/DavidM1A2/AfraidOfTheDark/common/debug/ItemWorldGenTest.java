/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.debug;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncParticleFX;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDParticleFXTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ItemWorldGenTest extends AOTDItem
{
	public ItemWorldGenTest()
	{
		super();
		this.setUnlocalizedName("worldGenTest");
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		//entityPlayer.openGui(Reference.MOD_ID, GuiHandler.SPELL_SELECTION_ID, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());

		TargetPoint particleCenter = new TargetPoint(entityPlayer.dimension, entityPlayer.posX, entityPlayer.posY + 1, entityPlayer.posZ, 40);
		if (!world.isRemote)
			for (int i = 0; i < 50; i++)
			{
				AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncParticleFX(AOTDParticleFXTypes.EnariaSplash, entityPlayer.posX, entityPlayer.posY + 1, entityPlayer.posZ), particleCenter);
			}
		LogHelper.info(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getHasBeatenEnaria());

		return super.onItemRightClick(itemStack, world, entityPlayer);
	}
}
