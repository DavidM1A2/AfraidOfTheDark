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
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;

import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
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
			// Reset motion
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				double x = entityPlayer.posX;
				double y = entityPlayer.posY;
				double z = entityPlayer.posZ;
				// Nether divide coords by 8
				if (this.dimensionOld == -1)
				{
					x = x / 8;
					z = z / 8;
				}

				// Find a good place to place the player once returning
				for (int i = MathHelper.ceil(x) - DISTANCE_TO_SEARCH_FOR_SPAWN_FOR / 2; i < MathHelper.ceil(x) + DISTANCE_TO_SEARCH_FOR_SPAWN_FOR; i++)
					for (int j = MathHelper.ceil(y) - DISTANCE_TO_SEARCH_FOR_SPAWN_FOR / 2; j < MathHelper.ceil(y) + DISTANCE_TO_SEARCH_FOR_SPAWN_FOR; j++)
						for (int k = MathHelper.ceil(z) - DISTANCE_TO_SEARCH_FOR_SPAWN_FOR / 2; k < MathHelper.ceil(z) + DISTANCE_TO_SEARCH_FOR_SPAWN_FOR; k++)
							if (isValidSpawnLocation(this.worldServerInstance.getMinecraftServer().getWorld(this.dimensionOld), new BlockPos(i, j, k)))
							{
								entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationPreTeleport(new Point3D(i, j + 1, k), this.dimensionOld);
								int locationX = this.getValidatePlayerLocationVoidChest(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationVoidChest(), entityPlayer) * AOTDDimensions.getBlocksBetweenIslands();
								((EntityPlayerMP) entityPlayer).connection.setPlayerLocation(locationX + 25, 104, 3, 0, 0);
								return;
							}

				// We didn't find a good spot. Error.
				entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationPreTeleport(new Point3D(0, 255, 0), this.dimensionOld);
				entityPlayer.sendMessage(new TextComponentString("An error occoured when saving your location in your previous dimension. When you return you will be placed at 0, 255, 0"));
				int locationX = this.getValidatePlayerLocationVoidChest(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationVoidChest(), entityPlayer) * AOTDDimensions.getBlocksBetweenIslands();
				((EntityPlayerMP) entityPlayer).connection.setPlayerLocation(locationX + 24.5, 104, 3, 0, 0);
			}
		}
		else if (dimensionOld == AOTDDimensions.VoidChest.getWorldID())
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				BlockPos playerPostionOld = entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationPreTeleport().toBlockPos();
				if (playerPostionOld.getY() == 255)
					entityPlayer.sendMessage(new TextComponentString("There were no air blocks surrounding the portal you originally passed through. Defaulting to 0, 255, 0"));
				((EntityPlayerMP) entityPlayer).connection.setPlayerLocation(playerPostionOld.getX() + 0.5, playerPostionOld.getY() + 1, playerPostionOld.getZ() + 0.5, 0, 0);
			}
		}
	}

	private boolean isValidSpawnLocation(World world, BlockPos blockPos)
	{
		if (!(world.getBlockState(blockPos).getBlock() instanceof BlockAir) && !(world.getBlockState(blockPos).getBlock() instanceof BlockVoidChestPortal))
			if (world.getBlockState(blockPos.add(0, 1, 0)).getBlock() instanceof BlockAir && world.getBlockState(blockPos.add(0, 2, 0)).getBlock() instanceof BlockAir)
				return true;
		return false;
	}

	private int getValidatePlayerLocationVoidChest(int locationX, EntityPlayer entityPlayer)
	{
		if (locationX == 0)
		{
			if (!entityPlayer.world.isRemote)
			{
				entityPlayer.world.getMinecraftServer().getCommandManager().executeCommand(entityPlayer.world.getMinecraftServer(), "/save-all");
			}

			int furthestOutPlayer = 0;
			for (NBTTagCompound entityPlayerData : NBTHelper.getOfflinePlayerNBTs())
			{
				furthestOutPlayer = Math.max(furthestOutPlayer, AOTDPlayerData.getPlayerLocationVoidChestOffline(entityPlayerData));
			}
			for (EntityPlayer entityPlayerOther : entityPlayer.world.getMinecraftServer().getPlayerList().getPlayers())
			{
				if (!entityPlayer.isEntityEqual(entityPlayerOther))
					furthestOutPlayer = Math.max(furthestOutPlayer, entityPlayerOther.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationVoidChest());
			}
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationVoidChest(furthestOutPlayer + 1);
		}
		return entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationVoidChest();
	}
}
