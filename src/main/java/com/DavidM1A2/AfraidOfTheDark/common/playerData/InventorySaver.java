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
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.IExtendedEntityProperties;

public class InventorySaver implements IExtendedEntityProperties
{
	public final static String INVENTORY_SAVER = "inventorySaver";
	public final static String PLAYER_LOCATION_OVERWORLD = "playerLocationOverworld";
	public final static String PLAYER_LOCATION_NIGHTMARE = "playerLocationNightmare";
	private NBTTagList inventoryList = new NBTTagList();
	private int[] playerLocationOverworld = new int[]
	{ 0, 255, 0 };
	private int playerLocationNightmare = -1;

	public static final void register(final EntityPlayer entityPlayer)
	{
		if (entityPlayer.getExtendedProperties(InventorySaver.INVENTORY_SAVER) == null)
		{
			entityPlayer.registerExtendedProperties(InventorySaver.INVENTORY_SAVER, new InventorySaver());
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
		compound.setTag(INVENTORY_SAVER, inventoryList);
		compound.setIntArray(PLAYER_LOCATION_OVERWORLD, playerLocationOverworld);
		compound.setInteger(PLAYER_LOCATION_NIGHTMARE, playerLocationNightmare);
	}

	// load works the same way
	@Override
	public void loadNBTData(final NBTTagCompound compound)
	{
		this.inventoryList = compound.getTagList(INVENTORY_SAVER, 10);
		this.playerLocationOverworld = compound.getIntArray(PLAYER_LOCATION_OVERWORLD);
		this.playerLocationNightmare = compound.getInteger(PLAYER_LOCATION_NIGHTMARE);
	}

	public static void loadInventory(final EntityPlayer entityPlayer)
	{
		entityPlayer.inventory.readFromNBT(entityPlayer.getEntityData().getTagList(INVENTORY_SAVER, 10));
	}

	public static NBTTagList getInventory(final EntityPlayer entityPlayer)
	{
		entityPlayer.inventory.readFromNBT(entityPlayer.getEntityData().getTagList(INVENTORY_SAVER, 10));
		return entityPlayer.inventory.writeToNBT(new NBTTagList());
	}

	public static BlockPos getPlayerLocationOverworld(final EntityPlayer entityPlayer)
	{
		int[] location = entityPlayer.getEntityData().getIntArray(PLAYER_LOCATION_OVERWORLD);
		if (location[1] == 0)
		{
			location[1] = Utility.getPlaceToSpawnLowest(entityPlayer.worldObj, 0, 0, 0, 0) + 2;
			LogHelper.error("Player data incorrectly saved. Defaulting to 0, 0. Please report this to the mod author.");
		}
		return new BlockPos(location[0], location[1], location[2]);
	}

	public static int getPlayerLocationNightmare(final EntityPlayer entityPlayer)
	{
		if (!entityPlayer.getEntityData().hasKey(PLAYER_LOCATION_NIGHTMARE))
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
							furthestOutPlayer = Math.max(furthestOutPlayer, playerDataCompound.getCompoundTag("ForgeData").getInteger(InventorySaver.PLAYER_LOCATION_NIGHTMARE));
						}
						catch (Exception e)
						{
							LogHelper.info("Error reading player data for username " + username);
						}
					}
				}

				InventorySaver.setPlayerLocationNightmare(entityPlayer, furthestOutPlayer + 1);
			}
		}
		return entityPlayer.getEntityData().getInteger(PLAYER_LOCATION_NIGHTMARE);
	}

	public static void setPlayerLocationNightmare(final EntityPlayer entityPlayer, int value)
	{
		entityPlayer.getEntityData().setInteger(PLAYER_LOCATION_NIGHTMARE, value);
	}

	public static void resetSavedInventory(final EntityPlayer entityPlayer)
	{
		entityPlayer.getEntityData().setTag(INVENTORY_SAVER, new NBTTagCompound());
	}

	public static void saveInventory(final EntityPlayer entityPlayer)
	{
		NBTTagList inventoryList = new NBTTagList();
		entityPlayer.inventory.writeToNBT(inventoryList);
		entityPlayer.getEntityData().setTag(INVENTORY_SAVER, inventoryList);
		entityPlayer.getEntityData().setIntArray(PLAYER_LOCATION_OVERWORLD, new int[]
		{ entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY() + 1, entityPlayer.getPosition().getZ() });
	}

	public static void set(final EntityPlayer entityPlayer, NBTTagList inventory, BlockPos overworldLocation, int nightmareLocation)
	{
		entityPlayer.inventory.writeToNBT(inventory);
		entityPlayer.getEntityData().setTag(INVENTORY_SAVER, inventory);
		entityPlayer.getEntityData().setIntArray(PLAYER_LOCATION_OVERWORLD, new int[]
		{ overworldLocation.getX(), overworldLocation.getY() + 1, overworldLocation.getZ() });
		entityPlayer.getEntityData().setInteger(PLAYER_LOCATION_NIGHTMARE, nightmareLocation);
	}
}
