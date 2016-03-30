/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest;

import com.DavidM1A2.AfraidOfTheDark.common.block.BlockVoidChestPortal;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.playerData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.WorldGenerationUtility;

import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class VoidChestTeleporter extends Teleporter
{
	private final WorldServer worldServerInstance;
	private final int dimensionOld;
	private final int dimensionNew;
	private static final int DISTANCE_TO_SEARCH_FOR_SPAWN_FOR = 7;

	public VoidChestTeleporter(WorldServer worldIn, int dimensionOld, int dimensionNew)
	{
		super(worldIn);
		this.worldServerInstance = worldIn;
		this.dimensionOld = dimensionOld;
		this.dimensionNew = dimensionNew;
	}

	@Override
	public void placeInPortal(Entity entity, float entityYaw)
	{
		if (dimensionNew == AOTDDimensions.VoidChest.getWorldID())
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;

				for (int i = MathHelper.ceiling_double_int(entityPlayer.posX) - DISTANCE_TO_SEARCH_FOR_SPAWN_FOR / 2; i < MathHelper.ceiling_double_int(entityPlayer.posX) + DISTANCE_TO_SEARCH_FOR_SPAWN_FOR; i++)
				{
					for (int j = MathHelper.ceiling_double_int(entityPlayer.posY) - DISTANCE_TO_SEARCH_FOR_SPAWN_FOR / 2; j < MathHelper.ceiling_double_int(entityPlayer.posY) + DISTANCE_TO_SEARCH_FOR_SPAWN_FOR; j++)
					{
						for (int k = MathHelper.ceiling_double_int(entityPlayer.posZ) - DISTANCE_TO_SEARCH_FOR_SPAWN_FOR / 2; k < MathHelper.ceiling_double_int(entityPlayer.posZ) + DISTANCE_TO_SEARCH_FOR_SPAWN_FOR; k++)
						{
							if (isValidSpawnLocation(entityPlayer.worldObj, new BlockPos(i, j, k)))
							{
								entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationOverworld(new int[] { i, j + 1, k });

								int locationX = this.validatePlayerLocationVoidChest(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationVoidChest(), entityPlayer) * AOTDDimensions.getBlocksBetweenIslands() + 25;

								((EntityPlayerMP) entityPlayer).playerNetServerHandler.setPlayerLocation(locationX, 104, 3, 0, 0);

								return;
							}
						}
					}
				}

				if (!entityPlayer.worldObj.isRemote)
				{
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationOverworld(new int[] { 0, WorldGenerationUtility.getFirstNonAirBlock(MinecraftServer.getServer().worldServerForDimension(0), 0, 0), 0 });
				}
				else
				{
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationOverworld(new int[] { 0, 255, 0 });
				}

				int locationX = this.validatePlayerLocationVoidChest(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationVoidChest(), entityPlayer) * AOTDDimensions.getBlocksBetweenIslands() + 25;

				((EntityPlayerMP) entityPlayer).playerNetServerHandler.setPlayerLocation(locationX, 104, 3, 0, 0);

			}
		}
		else if (dimensionOld == AOTDDimensions.VoidChest.getWorldID())
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				BlockPos playerPostionOld = this.intArrToBlockPos(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationOverworld(), entityPlayer);

				if (playerPostionOld.getX() == 0 && playerPostionOld.getZ() == 0)
				{
					entityPlayer.addChatMessage(new ChatComponentText("There were no air blocks surrounding the portal you passed through. Defaulting to 0, 0"));
				}
				entityPlayer.setPosition(playerPostionOld.getX() + 0.5, playerPostionOld.getY() + 1, playerPostionOld.getZ() + 0.5);
			}
		}
	}

	private boolean isValidSpawnLocation(World world, BlockPos blockPos)
	{
		if (!(world.getBlockState(blockPos).getBlock() instanceof BlockAir) && !(world.getBlockState(blockPos).getBlock() instanceof BlockVoidChestPortal))
		{
			if (world.getBlockState(blockPos.add(0, 1, 0)).getBlock() instanceof BlockAir && world.getBlockState(blockPos.add(0, 2, 0)).getBlock() instanceof BlockAir)
			{
				return true;
			}
		}
		return false;
	}

	private BlockPos intArrToBlockPos(int[] location, EntityPlayer entityPlayer)
	{
		if (location.length == 0)
		{
			return new BlockPos(0, WorldGenerationUtility.getFirstNonAirBlock(entityPlayer.worldObj, 0, 0) + 2, 0);
		}

		if (location[1] == 0)
		{
			location[1] = WorldGenerationUtility.getFirstNonAirBlock(entityPlayer.worldObj, 0, 0) + 2;
			LogHelper.error("Player data incorrectly saved. Defaulting to 0, 0. Please report this to the mod author.");
		}
		return new BlockPos(location[0], location[1], location[2]);
	}

	private int validatePlayerLocationVoidChest(int locationX, EntityPlayer entityPlayer)
	{
		if (locationX == 0)
		{
			if (!entityPlayer.worldObj.isRemote)
			{
				MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "/save-all");
			}

			int furthestOutPlayer = 0;
			for (NBTTagCompound entityPlayerData : NBTHelper.getOfflinePlayerNBTs())
			{
				furthestOutPlayer = Math.max(furthestOutPlayer, AOTDPlayerData.getPlayerLocationVoidChestOffline(entityPlayerData));
			}
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationVoidChest(furthestOutPlayer + 1);
		}
		return entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationVoidChest();
	}
}
