/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.VoidChestLocation;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;

public class VoidChestTeleporter extends Teleporter
{
	private final WorldServer worldServerInstance;
	private final int dimensionOld;
	private final int dimensionNew;

	public VoidChestTeleporter(WorldServer worldIn, int dimensionOld, int dimensionNew)
	{
		super(worldIn);
		this.worldServerInstance = worldIn;
		this.dimensionOld = dimensionOld;
		this.dimensionNew = dimensionNew;
	}

	@Override
	public void func_180266_a(Entity entity, float entityYaw)
	{
		if (dimensionNew == Constants.VoidChestWorld.VOID_CHEST_WORLD_ID)
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				VoidChestLocation.setOverworldLocation(entityPlayer, new int[]
				{ (int) entityPlayer.posX, (int) entityPlayer.posY + 1, (int) entityPlayer.posZ });

				if (VoidChestLocation.getVoidChestLocation(entityPlayer) == -1)
				{
					ISaveHandler iSaveHandler = MinecraftServer.getServer().worldServers[0].getSaveHandler();
					if (iSaveHandler instanceof SaveHandler)
					{
						VoidChestLocation.setVoidChestLocation(entityPlayer, ((SaveHandler) iSaveHandler).getAvailablePlayerDat().length - 1);
					}
				}
				((EntityPlayerMP) entityPlayer).playerNetServerHandler.setPlayerLocation(VoidChestLocation.getVoidChestLocation(entityPlayer) * Constants.VoidChestWorld.BLOCKS_BETWEEN_ISLANDS + 24.5, 110, 3, 0, 0);
			}
		}
		else if (dimensionOld == Constants.VoidChestWorld.VOID_CHEST_WORLD_ID)
		{
			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				BlockPos playerPostionOld = VoidChestLocation.getOverworldLocation(entityPlayer);
				entityPlayer.setPosition(playerPostionOld.getX(), playerPostionOld.getY() + 1, playerPostionOld.getZ());
				entity.motionX = 0.0D;
				entity.motionY = 0.0D;
				entity.motionZ = 0.0D;
			}
		}
	}
}
