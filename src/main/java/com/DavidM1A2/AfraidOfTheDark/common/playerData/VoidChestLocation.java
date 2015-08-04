/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.playerData;

import java.io.File;
import java.io.FileInputStream;

import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.IExtendedEntityProperties;

public class VoidChestLocation implements IExtendedEntityProperties
{
	private int[] playerLocationOverworld = new int[]
	{ 0, 255, 0 };
	private int playerLocationVoidChest = -1;
	public final static String VOID_CHEST_LOCATION = "voidChestLocation";
	public final static String PLAYER_LOCATION_VOID_CHEST = "playerLocationVoidChest";
	public final static String PLAYER_LOCATION_OVERWORLD = "playerLocationOverworld";

	public static final void register(final EntityPlayer entityPlayer)
	{
		/*
		 * This second block of code will check if the player has begun the mod.
		 */
		if (entityPlayer.getExtendedProperties(VoidChestLocation.VOID_CHEST_LOCATION) == null)
		{
			entityPlayer.registerExtendedProperties(VoidChestLocation.VOID_CHEST_LOCATION, new VoidChestLocation());
		}
	}

	@Override
	public void init(final Entity entity, final World world)
	{
	}

	// saveNBTData is called whenever data needs to be saved
	@Override
	public void saveNBTData(final NBTTagCompound compound)
	{
		compound.setIntArray(PLAYER_LOCATION_OVERWORLD, this.playerLocationOverworld);
		compound.setInteger(PLAYER_LOCATION_VOID_CHEST, this.playerLocationVoidChest);
	}

	// load works the same way
	@Override
	public void loadNBTData(final NBTTagCompound compound)
	{
		this.playerLocationOverworld = compound.getIntArray(PLAYER_LOCATION_OVERWORLD);
		this.playerLocationVoidChest = compound.getInteger(PLAYER_LOCATION_VOID_CHEST);
	}

	// Getters and setters for if a player has begun AOTD
	public static BlockPos getOverworldLocation(final EntityPlayer entityPlayer)
	{
		int[] location = entityPlayer.getEntityData().getIntArray(PLAYER_LOCATION_OVERWORLD);
		if (location.length == 0)
		{
			return new BlockPos(0, Utility.getFirstNonAirBlock(entityPlayer.worldObj, 0, 0) + 2, 0);
		}
		return new BlockPos(location[0], location[1], location[2]);
	}

	// Getters and setters for if a player has begun AOTD
	public static int getVoidChestLocation(final EntityPlayer entityPlayer)
	{
		if (!entityPlayer.getEntityData().hasKey(PLAYER_LOCATION_VOID_CHEST))
		{
			if (!entityPlayer.worldObj.isRemote)
			{
				MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "/save-all");
			}

			ISaveHandler iSaveHandler = MinecraftServer.getServer().worldServers[0].getSaveHandler();
			if (iSaveHandler instanceof SaveHandler)
			{
				SaveHandler saveHandler = (SaveHandler) iSaveHandler;
				int furthestOutPlayer = 0;
				File playersDirectory = new File(saveHandler.getWorldDirectory(), "playerdata");
				for (String username : saveHandler.getAvailablePlayerDat())
				{
					File playerData = new File(playersDirectory, username + ".dat");

					if (playerData.exists() && playerData.isFile())
					{
						NBTTagCompound playerDataCompound;
						try
						{
							playerDataCompound = CompressedStreamTools.readCompressed(new FileInputStream(playerData));
							furthestOutPlayer = Math.max(furthestOutPlayer, playerDataCompound.getCompoundTag("ForgeData").getInteger(VoidChestLocation.PLAYER_LOCATION_VOID_CHEST));
						}
						catch (Exception e)
						{
							LogHelper.info("Error reading player data for username " + username);
						}
					}
				}

				VoidChestLocation.setVoidChestLocation(entityPlayer, furthestOutPlayer + 1);
			}
		}
		return entityPlayer.getEntityData().getInteger(PLAYER_LOCATION_VOID_CHEST);
	}

	public static void setOverworldLocation(final EntityPlayer entityPlayer, final int[] overworldLocation)
	{
		entityPlayer.getEntityData().setIntArray(PLAYER_LOCATION_OVERWORLD, overworldLocation);
	}

	public static void setVoidChestLocation(final EntityPlayer entityPlayer, final int voidChestLocation)
	{
		entityPlayer.getEntityData().setInteger(PLAYER_LOCATION_VOID_CHEST, voidChestLocation);
	}
}
