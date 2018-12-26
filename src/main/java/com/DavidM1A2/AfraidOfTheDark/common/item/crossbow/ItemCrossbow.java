/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.crossbow;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDCrossbowBoltTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// The crossbow Item
public class ItemCrossbow extends AOTDItem
{
	// Keep a loaded and unloaded icon and store reload time
	private final int RELOAD_TIME = 50;

	public ItemCrossbow()
	{
		// 1 bow per itemstack
		super();
		this.setUnlocalizedName("crossbow");
		this.setMaxStackSize(1);
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
	 */
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (!isSelected)
		{
			if (itemStack.getItemDamage() == 2 || itemStack.getItemDamage() == 1)
			{
				itemStack.setItemDamage(0);
			}
		}
	}

	// On swing (left click) if the bow is cocked we fire
	@Override
	public boolean onEntitySwing(final EntityLivingBase entityLiving, final ItemStack itemStack)
	{
		if (!entityLiving.worldObj.isRemote)
		{
			if (entityLiving instanceof EntityPlayer)
			{
				if (itemStack.getItemDamage() == 3)
				{
					itemStack.setItemDamage(0);
					this.fireBolt((EntityPlayer) entityLiving, entityLiving.worldObj, itemStack);
				}
			}
		}

		return false;
	}

	// If the player is sneaking and right clicks, we change the mode.
	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (entityPlayer.isSneaking())
		{
			this.changeMode(itemStack, entityPlayer);
		}
		else
		{
			// If the player is not sneaking, we check to see if the bow is cocked or not
			if (itemStack.getItemDamage() == 0)
			{
				// If we are in creative, begin cocking he bow
				if (entityPlayer.capabilities.isCreativeMode)
				{
					entityPlayer.playSound("afraidofthedark:crossbowLoad", 0.9F, ((world.rand.nextFloat() * 0.8F) + 1.2F));
					entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
				}
				else
				{
					if (entityPlayer.inventory.hasItem(AOTDCrossbowBoltTypes.getTypeFromID(NBTHelper.getInt(itemStack, "mode")).getMyBoltItem()))
					{
						entityPlayer.playSound("afraidofthedark:crossbowLoad", 0.9F, ((world.rand.nextFloat() * 0.8F) + 1.2F));
						entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
					}
					else
					{
						if (world.isRemote)
						{
							// Else we print out that the player needs bolts to fire
							entityPlayer.addChatMessage(new ChatComponentText("I'll need at least one " + AOTDCrossbowBoltTypes.getTypeFromID(NBTHelper.getInt(itemStack, "mode")).formattedString() + "bolt in my inventory to shoot."));
						}
					}
				}
			}
		}
		return itemStack;
	}

	@Override
	public void onUsingTick(final ItemStack itemStack, final EntityPlayer entityPlayer, int count)
	{
		if (!entityPlayer.worldObj.isRemote)
		{
			count = this.getMaxItemUseDuration(itemStack) - count;
			// On using we play a sound
			if (count == 1)
			{
				itemStack.setItemDamage(1);
			}
			else if (count == RELOAD_TIME / 4)
			{
				itemStack.setItemDamage(1);
			}
			else if (count == RELOAD_TIME / 4 * 2)
			{
				itemStack.setItemDamage(2);
			}
			else if (count == RELOAD_TIME / 4 * 3)
			{
				itemStack.setItemDamage(2);
			}
			else if (count == RELOAD_TIME)
			{
				if (entityPlayer.capabilities.isCreativeMode || entityPlayer.inventory.consumeInventoryItem(AOTDCrossbowBoltTypes.getTypeFromID(NBTHelper.getInt(itemStack, "mode")).getMyBoltItem()))
				{
					itemStack.setItemDamage(3);
					entityPlayer.stopUsingItem();
				}
			}
		}
	}

	/**
	 * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is placed as a Block (mostly used with
	 * ItemBlocks).
	 */
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	// If the time in use is greater than 40, the bow was not completely cocked and we reset the pull animation
	@Override
	public void onPlayerStoppedUsing(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer, final int count)
	{
		if (itemStack.getItemDamage() != 3)
		{
			itemStack.setItemDamage(0);
		}
	}

	// Change mode sets the mode NBT tag on the crossbow
	public void changeMode(final ItemStack itemStack, final EntityPlayer entityPlayer)
	{
		if (itemStack.getItemDamage() != 3)
		{
			NBTHelper.setInteger(itemStack, "mode", AOTDCrossbowBoltTypes.getIDFromType(AOTDCrossbowBoltTypes.getTypeFromID(NBTHelper.getInt(itemStack, "mode")).next()));
			if (entityPlayer.worldObj.isRemote)
			{
				entityPlayer.addChatMessage(new ChatComponentText("Crossbow will fire: " + AOTDCrossbowBoltTypes.getTypeFromID(NBTHelper.getInt(itemStack, "mode")).formattedString() + "bolts."));
			}
		}
	}

	// When we fire, set the pull level of the bow to 0
	public void fireBolt(final EntityPlayer entityPlayer, final World world, final ItemStack itemStack)
	{
		world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, ((world.rand.nextFloat() * 0.4F) + 0.8F));
		world.spawnEntityInWorld(AOTDCrossbowBoltTypes.getTypeFromID(NBTHelper.getInt(itemStack, "mode")).createBolt(world, entityPlayer));
	}

	// A message under the bow will tell us what type of arrows the bow will fire
	@Override
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List list, final boolean bool)
	{
		list.add("Shift & Right click to change crossbow bolt type.");
		list.add("Bow will fire: " + AOTDCrossbowBoltTypes.getTypeFromID(NBTHelper.getInt(itemStack, "mode")) + " bolts.");
	}

	// Initialize the item when it is created
	@Override
	public void onCreated(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		NBTHelper.setInteger(itemStack, "mode", 0);
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack)
	{
		return RELOAD_TIME * 20;
	}

	// This item has a custom model, therefore it is full 3D
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
}
