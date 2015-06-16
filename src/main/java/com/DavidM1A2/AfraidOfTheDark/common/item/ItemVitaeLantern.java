/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Vitae;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

public class ItemVitaeLantern extends AOTDItem
{
	private static final int VITAE_CAPACITY = 300;
	private int ticksUpdated = 0;
	private final int UPDATE_FREQUENCY_IN_TICKS = 40;

	public ItemVitaeLantern()
	{
		super();
		this.setUnlocalizedName("vitaeLantern");
	}

	// If the player is sneaking and right clicks, we change the mode.
	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (NBTHelper.getBoolean(itemStack, "isActive"))
		{
			NBTHelper.setBoolean(itemStack, "isActive", false);
		}
		else
		{
			for (ItemStack itemStackCurrent : entityPlayer.inventory.mainInventory)
			{
				if (itemStackCurrent != null && itemStackCurrent.getItem() instanceof ItemVitaeLantern)
				{
					NBTHelper.setBoolean(itemStackCurrent, "isActive", false);
				}
			}
			NBTHelper.setBoolean(itemStack, "isActive", true);
		}

		return super.onItemRightClick(itemStack, world, entityPlayer);
	}

	/**
	 * Called when the player Left Clicks (attacks) an entity. Processed before damage is done, if return value is true further processing is canceled
	 * and the entity is not attacked.
	 *
	 * @param stack
	 *            The Item being used
	 * @param player
	 *            The player that is attacking
	 * @param entity
	 *            The entity being attacked
	 * @return True to cancel the rest of the interaction.
	 */
	@Override
	public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer entityPlayer, Entity entity)
	{
		if (entity instanceof EntityLivingBase)
		{
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

			if (!entityPlayer.worldObj.isRemote)
			{
				if (NBTHelper.getInt(itemStack, "storedVitae") >= 5)
				{
					int vitaeToTransfer = entityPlayer.worldObj.rand.nextInt(5) + 1;
					this.addVitae(itemStack, -vitaeToTransfer);
					Vitae.addVitae(entityLivingBase, vitaeToTransfer, Side.SERVER);
				}
				else if (NBTHelper.getInt(itemStack, "storedVitae") > 0)
				{
					int vitaeToTransfer = NBTHelper.getInt(itemStack, "storedVitae");
					this.addVitae(itemStack, -vitaeToTransfer);
					Vitae.addVitae(entityLivingBase, vitaeToTransfer, Side.SERVER);
				}
			}
		}

		return true;
	}

	/**
	 * Called when a player drops the item into the world, returning false from this will prevent the item from being removed from the players
	 * inventory and spawning in the world
	 *
	 * @param player
	 *            The player that dropped the item
	 * @param item
	 *            The item stack, before the item is removed.
	 */
	@Override
	public boolean onDroppedByPlayer(ItemStack itemStack, EntityPlayer entityPlayer)
	{
		NBTHelper.setBoolean(itemStack, "isActive", false);
		return super.onDroppedByPlayer(itemStack, entityPlayer);
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
	 */
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (ticksUpdated % UPDATE_FREQUENCY_IN_TICKS == 0)
		{
			if (NBTHelper.getBoolean(itemStack, "isActive"))
			{
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityPlayer = (EntityPlayer) entity;

					approachEqualibrium(itemStack, entityPlayer, MathHelper.floor_double(Constants.entityVitaeResistance.get(EntityPlayer.class) * NBTHelper.getDouble(itemStack, "equalibriumPercentage")));
				}
			}
			ticksUpdated = 1;
		}
		else
		{
			ticksUpdated = ticksUpdated + 1;
		}
	}

	private void approachEqualibrium(ItemStack itemStack, EntityPlayer entityPlayer, int equalibrium)
	{
		int currentVitae = Vitae.get(entityPlayer);

		if (Math.abs(currentVitae - equalibrium) <= 5)
		{
			if (addVitae(itemStack, currentVitae - equalibrium))
			{
				if (!entityPlayer.worldObj.isRemote)
				{
					Vitae.addVitae(entityPlayer, -(currentVitae - equalibrium), Side.SERVER);
				}
			}
		}
		else
		{
			if (addVitae(itemStack, currentVitae > equalibrium ? 5 : -5))
			{
				if (!entityPlayer.worldObj.isRemote)
				{
					Vitae.addVitae(entityPlayer, currentVitae > equalibrium ? -5 : 5, Side.SERVER);
				}
			}
		}
	}

	private boolean addVitae(ItemStack itemStack, int amount)
	{
		if (NBTHelper.getInt(itemStack, "storedVitae") + amount > this.VITAE_CAPACITY)
		{
			return false;
		}
		else if (NBTHelper.getInt(itemStack, "storedVitae") + amount < 0)
		{
			return false;
		}
		else
		{
			NBTHelper.setInteger(itemStack, "storedVitae", NBTHelper.getInt(itemStack, "storedVitae") + amount);
			return true;
		}
	}

	// A message under the bow will tell us what type of arrows the bow will fire
	@Override
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List list, final boolean bool)
	{
		list.add("Lantern is active? " + NBTHelper.getBoolean(itemStack, "isActive"));
		list.add("Lantern state: " + NBTHelper.getDouble(itemStack, "equalibriumPercentage") * 100 + "%");
		list.add("Stored vitae: " + NBTHelper.getInt(itemStack, "storedVitae"));
	}

	/**
	 * Determines if the durability bar should be rendered for this item. Defaults to vanilla stack.isDamaged behavior. But modders can use this for
	 * any data they wish.
	 *
	 * @param stack
	 *            The current Item Stack
	 * @return True if it should render the 'durability' bar.
	 */
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack
	 *            The current ItemStack
	 * @return 1.0 for 100% 0 for 0%
	 */
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1 - (double) NBTHelper.getInt(stack, "storedVitae") / (double) this.VITAE_CAPACITY;
	}
}
