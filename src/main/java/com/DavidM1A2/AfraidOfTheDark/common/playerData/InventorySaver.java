package com.DavidM1A2.AfraidOfTheDark.common.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class InventorySaver implements IExtendedEntityProperties
{
	public final static String INVENTORY_SAVER = "inventorySaver";
	public final static String PLAYER_LOCATION = "playerLocation";
	private NBTTagList inventoryList = new NBTTagList();
	private int[] playerLocation = new int[3];

	public static final void register(final EntityPlayer player)
	{
		player.registerExtendedProperties(InventorySaver.INVENTORY_SAVER, new InventorySaver());
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
		compound.setIntArray(PLAYER_LOCATION, playerLocation);
	}

	// load works the same way
	@Override
	public void loadNBTData(final NBTTagCompound compound)
	{
		this.inventoryList = compound.getTagList(INVENTORY_SAVER, 10);
		this.playerLocation = compound.getIntArray(PLAYER_LOCATION);
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

	public static BlockPos getPlayerLocation(final EntityPlayer entityPlayer)
	{
		int[] location = entityPlayer.getEntityData().getIntArray(PLAYER_LOCATION);
		return new BlockPos(location[0], location[1], location[2]);
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
		entityPlayer.getEntityData().setIntArray(PLAYER_LOCATION, new int[]
		{ entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY() + 1, entityPlayer.getPosition().getZ() });
	}

	public static void setInventory(final EntityPlayer entityPlayer, NBTTagList toSetTo, BlockPos location)
	{
		entityPlayer.inventory.writeToNBT(toSetTo);
		entityPlayer.getEntityData().setTag(INVENTORY_SAVER, toSetTo);
		entityPlayer.getEntityData().setIntArray(PLAYER_LOCATION, new int[]
		{ location.getX(), location.getY() + 1, location.getZ() });
	}
}
