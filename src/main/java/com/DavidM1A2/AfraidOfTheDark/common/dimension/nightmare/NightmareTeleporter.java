/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.InventorySaver;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class NightmareTeleporter extends Teleporter
{
	private final WorldServer worldServerInstance;
	private final int dimensionOld;
	private final int dimensionNew;

	public NightmareTeleporter(WorldServer worldIn, int dimensionOld, int dimensionNew)
	{
		super(worldIn);
		this.worldServerInstance = worldIn;
		this.dimensionOld = dimensionOld;
		this.dimensionNew = dimensionNew;
	}

	@Override
	public void func_180266_a(Entity entity, float entityYaw)
	{
		if (dimensionNew == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				if (!entity.worldObj.isRemote)
				{
					EntityPlayer entityPlayer = (EntityPlayer) entity;
					InventorySaver.saveInventory(entityPlayer);
					entityPlayer.inventory.clear();
					entityPlayer.inventoryContainer.detectAndSendChanges();

					((EntityPlayerMP) entityPlayer).playerNetServerHandler.setPlayerLocation(InventorySaver.getPlayerLocationNightmare(entityPlayer) * Constants.NightmareWorld.BLOCKS_BETWEEN_ISLANDS + 20, 79, 40, 0, 0);
				}
			}
		}
		else if (dimensionOld == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;

				BlockPos playerPostionOld = InventorySaver.getPlayerLocationOverworld(entityPlayer);
				entityPlayer.setPosition(playerPostionOld.getX(), playerPostionOld.getY(), playerPostionOld.getZ());
				entity.motionX = 0.0D;
				entity.motionY = 0.0D;
				entity.motionZ = 0.0D;

				InventorySaver.loadInventory(entityPlayer);
				InventorySaver.resetSavedInventory(entityPlayer);
			}
		}
	}
}
