/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class NBTHelper
{
	public static List<NBTTagCompound> getOfflinePlayerNBTs()
	{
		List<NBTTagCompound> toReturn = new LinkedList<NBTTagCompound>();

		ISaveHandler iSaveHandler = FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0].getSaveHandler();
		if (iSaveHandler instanceof SaveHandler)
		{
			SaveHandler saveHandler = (SaveHandler) iSaveHandler;
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
						if (playerDataCompound != null)
							toReturn.add(playerDataCompound);
					}
					catch (Exception e)
					{
						LogHelper.info("Error reading player data for username " + username);
					}
				}
			}
		}

		return toReturn;
	}

	public static boolean hasTag(final ItemStack itemStack, final String keyName)
	{
		return (itemStack != null) && (itemStack.getTagCompound() != null) && itemStack.getTagCompound().hasKey(keyName);
	}

	public static void removeTag(final ItemStack itemStack, final String keyName)
	{
		if (itemStack.getTagCompound() != null)
		{
			itemStack.getTagCompound().removeTag(keyName);
		}
	}

	/**
	 * Initializes the NBT Tag Compound for the given ItemStack if it is null
	 *
	 * @param itemStack
	 *            The ItemStack for which its NBT Tag Compound is being checked for initialization
	 */
	private static void initNBTTagCompound(final ItemStack itemStack)
	{
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}
	}

	public static void setLong(final ItemStack itemStack, final String keyName, final long keyValue)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setLong(keyName, keyValue);
	}

	// String
	public static String getString(final ItemStack itemStack, final String keyName)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName))
		{
			NBTHelper.setString(itemStack, keyName, "");
		}

		return itemStack.getTagCompound().getString(keyName);
	}

	public static void setString(final ItemStack itemStack, final String keyName, final String keyValue)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setString(keyName, keyValue);
	}

	// boolean
	public static boolean getBoolean(final ItemStack itemStack, final String keyName)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName))
		{
			NBTHelper.setBoolean(itemStack, keyName, false);
		}

		return itemStack.getTagCompound().getBoolean(keyName);
	}

	public static void setBoolean(final ItemStack itemStack, final String keyName, final boolean keyValue)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setBoolean(keyName, keyValue);
	}

	// byte
	public static byte getByte(final ItemStack itemStack, final String keyName)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName))
		{
			NBTHelper.setByte(itemStack, keyName, (byte) 0);
		}

		return itemStack.getTagCompound().getByte(keyName);
	}

	public static void setByte(final ItemStack itemStack, final String keyName, final byte keyValue)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setByte(keyName, keyValue);
	}

	// short
	public static short getShort(final ItemStack itemStack, final String keyName)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName))
		{
			NBTHelper.setShort(itemStack, keyName, (short) 0);
		}

		return itemStack.getTagCompound().getShort(keyName);
	}

	public static void setShort(final ItemStack itemStack, final String keyName, final short keyValue)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setShort(keyName, keyValue);
	}

	// int
	public static int getInt(final ItemStack itemStack, final String keyName)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName))
		{
			NBTHelper.setInteger(itemStack, keyName, 0);
		}

		return itemStack.getTagCompound().getInteger(keyName);
	}

	public static void setInteger(final ItemStack itemStack, final String keyName, final int keyValue)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setInteger(keyName, keyValue);
	}

	// long
	public static long getLong(final ItemStack itemStack, final String keyName)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName))
		{
			NBTHelper.setLong(itemStack, keyName, 0);
		}

		return itemStack.getTagCompound().getLong(keyName);
	}

	// float
	public static float getFloat(final ItemStack itemStack, final String keyName)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName))
		{
			NBTHelper.setFloat(itemStack, keyName, 0);
		}

		return itemStack.getTagCompound().getFloat(keyName);
	}

	public static void setFloat(final ItemStack itemStack, final String keyName, final float keyValue)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setFloat(keyName, keyValue);
	}

	// double
	public static double getDouble(final ItemStack itemStack, final String keyName)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		if (!itemStack.getTagCompound().hasKey(keyName))
		{
			NBTHelper.setDouble(itemStack, keyName, 0);
		}

		return itemStack.getTagCompound().getDouble(keyName);
	}

	public static void setDouble(final ItemStack itemStack, final String keyName, final double keyValue)
	{
		NBTHelper.initNBTTagCompound(itemStack);

		itemStack.getTagCompound().setDouble(keyName, keyValue);
	}
}