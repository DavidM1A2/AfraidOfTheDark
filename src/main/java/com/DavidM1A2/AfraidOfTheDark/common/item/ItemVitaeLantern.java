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
import net.minecraft.nbt.NBTTagCompound;
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
				for (ItemStack itemStackCurrent : entityPlayer.inventory.mainInventory)
				{
					if (itemStackCurrent != null && itemStackCurrent.getItem() instanceof ItemVitaeLantern)
					{
						NBTHelper.setBoolean(itemStackCurrent, "isActive", false);
					}
				}
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
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise the damage on the stack.
	 * 
	 * @param target
	 *            The Entity being hit
	 * @param attacker
	 *            the attacking entity
	 */
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (NBTHelper.getInt(itemStack, "storedVitae") >= 5)
		{
			int vitaeToTransfer = attacker.worldObj.rand.nextInt(5) + 1;
			this.addVitae(itemStack, -vitaeToTransfer);
			Vitae.addVitae(target, vitaeToTransfer, null);
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
		if (!world.isRemote)
		{
			if (ticksUpdated % UPDATE_FREQUENCY_IN_TICKS == 0)
			{
				if (NBTHelper.getBoolean(itemStack, "isActive"))
				{
					if (entity instanceof EntityPlayer)
					{
						EntityPlayer entityPlayer = (EntityPlayer) entity;
						switch (this.currentState)
						{
							case High:
								approachEqualibrium(itemStack, entityPlayer, MathHelper.floor_double(Constants.entityVitaeResistance.get(EntityPlayer.class) * 0.90));
								break;
							case Low:
								approachEqualibrium(itemStack, entityPlayer, MathHelper.floor_double(Constants.entityVitaeResistance.get(EntityPlayer.class) * 0.20));
								break;
							case Medium:
								approachEqualibrium(itemStack, entityPlayer, MathHelper.floor_double(Constants.entityVitaeResistance.get(EntityPlayer.class) * 0.50));
								break;
							case None:
								approachEqualibrium(itemStack, entityPlayer, 0);
								break;
							case Obnoxious:
								approachEqualibrium(itemStack, entityPlayer, MathHelper.floor_double(Constants.entityVitaeResistance.get(EntityPlayer.class) * 0.99));
								break;
						}
					}
				}
				ticksUpdated = 1;
			}
			else
			{
				ticksUpdated = ticksUpdated + 1;
			}
		}
	}

	private void approachEqualibrium(ItemStack itemStack, EntityPlayer entityPlayer, int equalibrium)
	{
		int currentVitae = Vitae.get(entityPlayer);
		if (currentVitae > equalibrium)
		{
			if (addVitae(itemStack, 5))
			{
				Vitae.addVitae(entityPlayer, -5, Side.SERVER);
			}
		}
		else if (currentVitae < equalibrium)
		{
			if (addVitae(itemStack, -5))
			{
				Vitae.addVitae(entityPlayer, 5, Side.SERVER);
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
