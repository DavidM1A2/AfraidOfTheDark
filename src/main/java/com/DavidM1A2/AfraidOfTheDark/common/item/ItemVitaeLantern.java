/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

public class ItemVitaeLantern extends AOTDItem
{
	private static final int VITAE_CAPACITY = 300;
	private LanternStates currentState = LanternStates.Medium;
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
		if (!entityPlayer.isSneaking())
		{
			if (NBTHelper.getBoolean(itemStack, "isActive"))
			{
				NBTHelper.setBoolean(itemStack, "isActive", false);
			}
			else
			{
				NBTHelper.setBoolean(itemStack, "isActive", true);
			}
		}
		else
		{
			currentState = currentState.getNext();
			NBTHelper.setInteger(itemStack, "state", currentState.ordinal());
		}

		return super.onItemRightClick(itemStack, world, entityPlayer);
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
				switch (this.currentState)
				{
					case High:
						break;
					case Low:
						break;
					case Medium:
						break;
					case None:
						break;
					case Obnoxious:
						break;
				}
			}
		}
		else
		{
			ticksUpdated = ticksUpdated + 1;
		}
	}

	/**
	 * Called when an ItemStack with NBT data is read to potentially that ItemStack's NBT data
	 */
	public boolean updateItemStackNBT(NBTTagCompound nbt)
	{
		this.currentState = LanternStates.values()[nbt.getInteger("state")];
		return super.updateItemStackNBT(nbt);
	}

	// A message under the bow will tell us what type of arrows the bow will fire
	@Override
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List list, final boolean bool)
	{
		list.add("Lantern is active? " + NBTHelper.getBoolean(itemStack, "isActive"));
		list.add("Lantern state: " + this.currentState.toString());
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
		return (double) this.VITAE_CAPACITY / NBTHelper.getInt(stack, "storedVitae");
	}

	public enum LanternStates
	{
		None,
		Low,
		Medium,
		High,
		Obnoxious;

		public LanternStates getNext()
		{
			return values()[(ordinal() + 1) % values().length];
		}
	}
}
