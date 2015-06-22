package com.DavidM1A2.AfraidOfTheDark.common.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class InventorySaver implements IExtendedEntityProperties
{
	public final static String INVENTORY_SAVER = "inventorySaver";
	private NBTTagList inventoryList = new NBTTagList();

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
	}

	// load works the same way
	@Override
	public void loadNBTData(final NBTTagCompound compound)
	{
		this.inventoryList = compound.getTagList(INVENTORY_SAVER, 10);
	}

	public static NBTTagList loadInventory(final EntityPlayer entityPlayer)
	{
		entityPlayer.inventory.readFromNBT(entityPlayer.getEntityData().getTagList(INVENTORY_SAVER, 10));
		return entityPlayer.inventory.writeToNBT(new NBTTagList());
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
	}

	public static void saveInventory(final EntityPlayer entityPlayer, NBTTagList toSetTo)
	{
		entityPlayer.inventory.writeToNBT(toSetTo);
		entityPlayer.getEntityData().setTag(INVENTORY_SAVER, toSetTo);
	}
}
