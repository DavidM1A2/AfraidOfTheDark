package com.DavidM1A2.AfraidOfTheDark.common.reference;

import com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare.NightmareTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestTeleporter;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;

public enum AOTDDimensions
{
	Nightmare("nightmare", 67)
	{
		@Override
		public void toDimension(EntityPlayerMP entityPlayer)
		{
			this.teleportPlayer(entityPlayer, this.getWorldID(), new NightmareTeleporter(entityPlayer.getServerForPlayer(), entityPlayer.dimension, this.getWorldID()));
		}

		@Override
		public void fromDimensionTo(int dimension, EntityPlayerMP entityPlayer)
		{
			this.teleportPlayer(entityPlayer, dimension, new NightmareTeleporter(entityPlayer.getServerForPlayer(), this.getWorldID(), dimension));
		}
	},
	VoidChest("voidChest", 68)
	{
		@Override
		public void toDimension(EntityPlayerMP entityPlayer)
		{
			this.teleportPlayer(entityPlayer, this.getWorldID(), new VoidChestTeleporter(entityPlayer.getServerForPlayer(), entityPlayer.dimension, this.getWorldID()));
		}

		@Override
		public void fromDimensionTo(int dimension, EntityPlayerMP entityPlayer)
		{
			this.teleportPlayer(entityPlayer, dimension, new VoidChestTeleporter(entityPlayer.getServerForPlayer(), this.getWorldID(), dimension));
		}
	};

	private String worldName;
	private int worldID;
	private static final int BLOCKS_BETWEEN_ISLANDS = 992;

	private AOTDDimensions(String worldName, int worldID)
	{
		this.worldName = worldName;
		this.worldID = worldID;
	}

	public String getWorldName()
	{
		return this.worldName;
	}

	public int getWorldID()
	{
		return this.worldID;
	}

	public abstract void toDimension(EntityPlayerMP entityPlayer);

	public abstract void fromDimensionTo(int dimension, EntityPlayerMP entityPlayer);

	protected void teleportPlayer(EntityPlayerMP entityPlayer, int dimension, Teleporter teleporter)
	{
		entityPlayer.mcServer.getConfigurationManager().transferPlayerToDimension(entityPlayer, dimension, teleporter);
	}

	public static int getBlocksBetweenIslands()
	{
		return BLOCKS_BETWEEN_ISLANDS;
	}
}
