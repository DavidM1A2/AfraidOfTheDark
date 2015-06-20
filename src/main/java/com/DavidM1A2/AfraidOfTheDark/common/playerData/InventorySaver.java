package com.DavidM1A2.AfraidOfTheDark.common.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class InventorySaver implements IExtendedEntityProperties
{
	public final static String INVENTORY_IDS = "inventoryIDs";
	public final static String INVENTORY_META = "inventoryMeta";
	public final static String INVENTORY_STACK_SIZE = "inventoryStackSize";
	public final static String INVENTORY_NBTS = "inventoryNBTS";
	public final static String INVENTORY_SAVER = "inventorySaver";
	private int[] itemIds = new int[40];
	private int[] itemMeta = new int[40];
	private int[] itemStackSize = new int[40];
	private NBTTagCompound[] itemNbtTagCompounds = new NBTTagCompound[40];

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
		compound.setIntArray(InventorySaver.INVENTORY_IDS, itemIds);
		compound.setIntArray(InventorySaver.INVENTORY_META, itemMeta);
		compound.setIntArray(InventorySaver.INVENTORY_STACK_SIZE, itemStackSize);
		for (int i = 0; i < itemNbtTagCompounds.length; i++)
		{
			if (itemNbtTagCompounds[i] != null)
			{
				compound.getCompoundTag(INVENTORY_NBTS).setTag(Integer.toString(i), itemNbtTagCompounds[i]);
			}
			else
			{
				compound.getCompoundTag(INVENTORY_NBTS).setTag(Integer.toString(i), new NBTTagCompound());
			}
		}
	}

	// load works the same way
	@Override
	public void loadNBTData(final NBTTagCompound compound)
	{
		itemIds = compound.getIntArray(InventorySaver.INVENTORY_IDS);
		itemMeta = compound.getIntArray(InventorySaver.INVENTORY_META);
		itemStackSize = compound.getIntArray(InventorySaver.INVENTORY_STACK_SIZE);
		for (int i = 0; i < 40; i++)
		{
			if (compound.getCompoundTag(INVENTORY_NBTS).getCompoundTag(Integer.toString(i)) != null)
			{
				itemNbtTagCompounds[i] = compound.getCompoundTag(INVENTORY_NBTS).getCompoundTag(Integer.toString(i));
			}
			else
			{
				itemNbtTagCompounds[i] = new NBTTagCompound();
			}
		}
	}

	public static ItemStack[] loadInventory(final EntityPlayer entityPlayer)
	{
		int[] itemIds = entityPlayer.getEntityData().getIntArray(INVENTORY_IDS);
		int[] itemMeta = entityPlayer.getEntityData().getIntArray(INVENTORY_META);
		int[] itemStackSize = entityPlayer.getEntityData().getIntArray(INVENTORY_STACK_SIZE);
		NBTTagCompound[] itemNbtTagCompounds = new NBTTagCompound[40];
		for (int i = 0; i < 36; i++)
		{
			itemNbtTagCompounds[i] = (NBTTagCompound) entityPlayer.getEntityData().getCompoundTag(INVENTORY_NBTS).getCompoundTag(Integer.toString(i));
		}

		ItemStack[] toReturn = new ItemStack[36];

		for (int i = 0; i < toReturn.length; i++)
		{
			if (itemIds[i] != -1)
			{
				toReturn[i] = new ItemStack(Item.getItemById(itemIds[i]), itemStackSize[i], itemMeta[i]);
				toReturn[i].setTagCompound(itemNbtTagCompounds[i]);
			}
			else
			{
				toReturn[i] = null;
			}
		}

		return toReturn;
	}

	public static ItemStack[] loadArmor(final EntityPlayer entityPlayer)
	{
		int[] itemIds = entityPlayer.getEntityData().getIntArray(INVENTORY_IDS);
		int[] itemMeta = entityPlayer.getEntityData().getIntArray(INVENTORY_META);
		int[] itemStackSize = entityPlayer.getEntityData().getIntArray(INVENTORY_STACK_SIZE);
		NBTTagCompound[] itemNbtTagCompounds = new NBTTagCompound[4];
		for (int i = 36; i < 40; i++)
		{
			itemNbtTagCompounds[i - 36] = (NBTTagCompound) entityPlayer.getEntityData().getCompoundTag(INVENTORY_NBTS).getCompoundTag(Integer.toString(i));
		}

		ItemStack[] toReturn = new ItemStack[4];

		for (int i = 0; i < toReturn.length; i++)
		{
			ItemStack current = toReturn[i];
			if (itemIds[i] != -1)
			{
				current = new ItemStack(Item.getItemById(itemIds[i + 36]), itemStackSize[i + 36], itemMeta[i + 36]);
				current.setTagCompound(itemNbtTagCompounds[i]);
			}
			else
			{
				current = null;
			}
		}

		return toReturn;
	}

	public static void save(final EntityPlayer entityPlayer)
	{
		int[] itemIds = new int[entityPlayer.inventory.mainInventory.length + entityPlayer.inventory.armorInventory.length];
		int[] itemMeta = new int[entityPlayer.inventory.mainInventory.length + entityPlayer.inventory.armorInventory.length];
		int[] itemStackSize = new int[entityPlayer.inventory.mainInventory.length + entityPlayer.inventory.armorInventory.length];
		NBTTagCompound[] itemNbtTagCompounds = new NBTTagCompound[entityPlayer.inventory.mainInventory.length + entityPlayer.inventory.armorInventory.length];
		for (int i = 0; i < entityPlayer.inventory.mainInventory.length; i++)
		{
			ItemStack current = entityPlayer.inventory.mainInventory[i];
			if (current != null)
			{
				itemIds[i] = Item.getIdFromItem(current.getItem());
				itemMeta[i] = current.getMetadata();
				itemStackSize[i] = current.stackSize;
				itemNbtTagCompounds[i] = current.getTagCompound();
			}
			else
			{
				itemIds[i] = -1;
				itemMeta[i] = -1;
				itemStackSize[i] = -1;
				itemNbtTagCompounds[i] = null;
			}
		}

		for (int i = 36; i < entityPlayer.inventory.armorInventory.length + 36; i++)
		{
			ItemStack current = entityPlayer.inventory.armorInventory[i - 36];
			if (current != null)
			{
				itemIds[i] = Item.getIdFromItem(current.getItem());
				itemMeta[i] = current.getMetadata();
				itemStackSize[i] = current.stackSize;
				itemNbtTagCompounds[i] = current.getTagCompound();
			}
			else
			{
				itemIds[i] = -1;
				itemMeta[i] = -1;
				itemStackSize[i] = -1;
				itemNbtTagCompounds[i] = null;
			}
		}

		entityPlayer.getEntityData().setIntArray(InventorySaver.INVENTORY_IDS, itemIds);
		entityPlayer.getEntityData().setIntArray(InventorySaver.INVENTORY_META, itemMeta);
		entityPlayer.getEntityData().setIntArray(InventorySaver.INVENTORY_STACK_SIZE, itemStackSize);
		for (int i = 0; i < itemNbtTagCompounds.length; i++)
		{
			entityPlayer.getEntityData().getCompoundTag(INVENTORY_NBTS).setTag(Integer.toString(i), itemNbtTagCompounds[i]);
		}
	}
}
