/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest;

import com.DavidM1A2.AfraidOfTheDark.common.block.BlockVoidChestPortal;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.VoidChestLocation;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.WorldGenerationUtility;

import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
	public void func_180266_a(Entity entity, float entityYaw)
	{
		if (dimensionNew == Constants.VoidChestWorld.VOID_CHEST_WORLD_ID)
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
								VoidChestLocation.setOverworldLocation(entityPlayer, new int[]
								{ i, j + 1, k });
								((EntityPlayerMP) entityPlayer).playerNetServerHandler.setPlayerLocation(VoidChestLocation.getVoidChestLocation(entityPlayer) * Constants.VoidChestWorld.BLOCKS_BETWEEN_ISLANDS + 24.5, 104, 3, 0, 0);
								return;
							}
						}
					}
				}

				if (!entityPlayer.worldObj.isRemote)
				{
					VoidChestLocation.setOverworldLocation(entityPlayer, new int[]
					{ 0, WorldGenerationUtility.getFirstNonAirBlock(MinecraftServer.getServer().worldServerForDimension(0), 0, 0), 0 });
				}
				else
				{
					VoidChestLocation.setOverworldLocation(entityPlayer, new int[]
					{ 0, 255, 0 });
				}
				((EntityPlayerMP) entityPlayer).playerNetServerHandler.setPlayerLocation(VoidChestLocation.getVoidChestLocation(entityPlayer) * Constants.VoidChestWorld.BLOCKS_BETWEEN_ISLANDS + 24.5, 104, 3, 0, 0);

			}
		}
		else if (dimensionOld == Constants.VoidChestWorld.VOID_CHEST_WORLD_ID)
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				BlockPos playerPostionOld = VoidChestLocation.getOverworldLocation(entityPlayer);
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
}
