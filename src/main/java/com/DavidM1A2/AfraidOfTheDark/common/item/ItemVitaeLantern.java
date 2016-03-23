/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDEntityData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemVitaeLantern extends AOTDItem
{
	public static final int VITAE_CAPACITY = 300;
	private int ticksUpdated = 0;
	private final int UPDATE_FREQUENCY_IN_TICKS = 80;
	public static final String STORED_VITAE = "storedVitae";

	public ItemVitaeLantern()
	{
		super();
		this.setUnlocalizedName("vitaeLantern");
		this.setMaxStackSize(1);
	}

	// If the player is sneaking and right clicks, we change the mode.
	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VitaeLanternI))
		{
			if (entityPlayer.isSneaking())
			{
				if (NBTHelper.getBoolean(itemStack, "isActive"))
				{
					NBTHelper.setBoolean(itemStack, "isActive", false);
					if (!world.isRemote)
					{
						entityPlayer.addChatMessage(new ChatComponentText("Lantern deactivated."));
					}
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
					if (!world.isRemote)
					{
						entityPlayer.addChatMessage(new ChatComponentText("Lantern activated."));
					}
				}
			}
		}
		else
		{
			if (world.isRemote)
			{
				entityPlayer.addChatMessage(new ChatComponentText("I'm uncertain of how to operate this device."));
			}
		}

		return super.onItemRightClick(itemStack, world, entityPlayer);
	}

	/**
	 * Returns true if the item can be used on the given entity, e.g. shears on
	 * sheep.
	 */
	@Override
	public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VitaeLanternI))
		{
			if (AOTDEntityData.get(entityLivingBase).getVitaeLevel() > 5 && !(entityLivingBase instanceof EntityPlayer))
			{
				int newVitae = AOTDEntityData.get(entityLivingBase).getVitaeLevel() - 5;
				if (AOTDEntityData.get(entityLivingBase).setVitaeLevel(newVitae))
				{
					entityLivingBase.worldObj.createExplosion(entityLivingBase, entityLivingBase.getPosition().getX(), entityLivingBase.getPosition().getY(), entityLivingBase.getPosition().getZ(), 2, true).doExplosionB(true);
					entityLivingBase.onKillCommand();
				}
				AOTDEntityData.get(entityLivingBase).syncVitaeLevel();

				// Itemstack here is wrong?
				addVitae(entityPlayer.getCurrentEquippedItem(), 5);
			}
		}

		return super.itemInteractionForEntity(itemStack, entityPlayer, entityLivingBase);
	}

	/**
	 * Called when the player Left Clicks (attacks) an entity. Processed before
	 * damage is done, if return value is true further processing is canceled
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
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VitaeLanternI))
		{
			if (entity instanceof EntityLivingBase)
			{
				EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

				if (!entityPlayer.worldObj.isRemote)
				{
					if (entityLivingBase instanceof EntityDeeeSyft)
					{
						if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.DeeeSyft))
						{
							entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.DeeeSyft, true);
						}
					}

					if (NBTHelper.getInt(itemStack, STORED_VITAE) >= 5)
					{
						int vitaeToTransfer = entityPlayer.worldObj.rand.nextInt(5) + 1;
						this.addVitae(itemStack, -vitaeToTransfer);

						int newVitae = AOTDEntityData.get(entityPlayer).getVitaeLevel() + vitaeToTransfer;
						if (AOTDEntityData.get(entityPlayer).setVitaeLevel(newVitae))
						{
							if (!entityPlayer.capabilities.isCreativeMode)
							{
								entityPlayer.worldObj.createExplosion(entityPlayer, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ(), 2, true).doExplosionB(true);
								entityPlayer.onKillCommand();
							}
						}
						AOTDEntityData.get(entityPlayer).syncVitaeLevel();
					}
					else if (NBTHelper.getInt(itemStack, STORED_VITAE) > 0)
					{
						int vitaeToTransfer = NBTHelper.getInt(itemStack, STORED_VITAE);
						this.addVitae(itemStack, -vitaeToTransfer);

						int newVitae = AOTDEntityData.get(entityPlayer).getVitaeLevel() + vitaeToTransfer;
						if (AOTDEntityData.get(entityPlayer).setVitaeLevel(newVitae))
						{
							if (!entityPlayer.capabilities.isCreativeMode)
							{
								entityPlayer.worldObj.createExplosion(entityPlayer, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ(), 2, true).doExplosionB(true);
								entityPlayer.onKillCommand();
							}
						}
						AOTDEntityData.get(entityPlayer).syncVitaeLevel();
					}
				}
			}
		}
		else
		{
			entityPlayer.addChatMessage(new ChatComponentText("The lantern is trying to operate but can't."));
		}

		return true;
	}

	/**
	 * Called when a player drops the item into the world, returning false from
	 * this will prevent the item from being removed from the players inventory
	 * and spawning in the world
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
	 * Called each tick as long the item is on a player inventory. Uses by maps
	 * to check if is on a player hand and update it's contents.
	 */
	@Override
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
		int currentVitae = AOTDEntityData.get(entityPlayer).getVitaeLevel();

		if (equalibrium != currentVitae)
		{
			if (Math.abs(currentVitae - equalibrium) <= 5)
			{
				if (addVitae(itemStack, currentVitae - equalibrium))
				{
					if (!entityPlayer.worldObj.isRemote)
					{
						int newVitae = currentVitae - (currentVitae - equalibrium);
						if (AOTDEntityData.get(entityPlayer).setVitaeLevel(newVitae))
						{
							if (!entityPlayer.capabilities.isCreativeMode)
							{
								entityPlayer.worldObj.createExplosion(entityPlayer, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ(), 2, entityPlayer.worldObj.getGameRules().getBoolean("mobGriefing"));
								entityPlayer.onKillCommand();
							}
						}
						AOTDEntityData.get(entityPlayer).syncVitaeLevel();
					}
				}
			}
			else
			{
				if (addVitae(itemStack, currentVitae > equalibrium ? 5 : -5))
				{
					if (!entityPlayer.worldObj.isRemote)
					{
						int newVitae = currentVitae + (currentVitae > equalibrium ? -5 : 5);
						if (AOTDEntityData.get(entityPlayer).setVitaeLevel(newVitae))
						{
							if (!entityPlayer.capabilities.isCreativeMode)
							{
								entityPlayer.worldObj.createExplosion(entityPlayer, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ(), 2, true).doExplosionB(true);
								entityPlayer.onKillCommand();
							}
						}
						AOTDEntityData.get(entityPlayer).syncVitaeLevel();
					}
				}
			}
		}
	}

	private boolean addVitae(ItemStack itemStack, int amount)
	{
		if (NBTHelper.getInt(itemStack, STORED_VITAE) + amount > ItemVitaeLantern.VITAE_CAPACITY)
		{
			return false;
		}
		else if (NBTHelper.getInt(itemStack, STORED_VITAE) + amount < 0)
		{
			return false;
		}
		else
		{
			NBTHelper.setInteger(itemStack, STORED_VITAE, NBTHelper.getInt(itemStack, STORED_VITAE) + amount);
			return true;
		}
	}

	// A message under the bow will tell us what type of arrows the bow will
	// fire
	@Override
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List list, final boolean bool)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VitaeLanternI))
		{
			list.add("Shift & Right click to toggle the lantern on and off.");
			list.add("Press and hold " + Keyboard.getKeyName(Keybindings.changeLanternMode.getKeyCode()) + " to set the vitae percentage");
			list.add("at which the lantern is to keep you at.");
			if (NBTHelper.getBoolean(itemStack, "isActive"))
			{
				list.add("Lantern is active.");
			}
			else
			{
				list.add("Lantern is not active.");
			}

			list.add("Lantern will attempt to keep you at " + Math.round(NBTHelper.getDouble(itemStack, "equalibriumPercentage") * 100) + "% of your maximum vitae.");
			list.add("Lantern is " + Math.round(100.0 * NBTHelper.getInt(itemStack, STORED_VITAE) / ItemVitaeLantern.VITAE_CAPACITY) + "% full.");
		}
		else
		{
			list.add("I'm not sure how to use this.");
		}
	}

	/**
	 * Converts the given ItemStack damage value into a metadata value to be
	 * placed in the world when this Item is placed as a Block (mostly used with
	 * ItemBlocks).
	 */
	@Override
	public int getMetadata(ItemStack itemStack)
	{
		int storedVitae = NBTHelper.getInt(itemStack, STORED_VITAE);
		return storedVitae < 5 ? 0 : storedVitae < 100 ? 1 : storedVitae < 200 ? 2 : storedVitae < 290 ? 3 : 4;
	}

	/**
	 * Determines if the durability bar should be rendered for this item.
	 * Defaults to vanilla stack.isDamaged behavior. But modders can use this
	 * for any data they wish.
	 *
	 * @param stack
	 *            The current Item Stack
	 * @return True if it should render the 'durability' bar.
	 */
	@Override
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
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1 - (double) NBTHelper.getInt(stack, STORED_VITAE) / (double) ItemVitaeLantern.VITAE_CAPACITY;
	}
}
