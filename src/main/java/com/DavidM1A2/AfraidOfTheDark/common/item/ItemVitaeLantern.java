/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemVitaeLantern extends AOTDItem
{
	public static final int VITAE_CAPACITY = 300;
	private int ticksUpdated = 0;
	private final int UPDATE_FREQUENCY_IN_TICKS = 80;
	private static final String STORED_VITAE = "storedVitae";

	public ItemVitaeLantern()
	{
		super();
		this.setUnlocalizedName("vitae_lantern");
		this.setRegistryName("vitae_lantern");
		this.setMaxStackSize(1);
	}

	/**
	 * Returns true if the item can be used on the given entity, e.g. shears on sheep.
	 */
	@Override
	public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase, EnumHand hand)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VitaeLanternI))
		{
			int entityVitae = entityLivingBase.getCapability(ModCapabilities.ENTITY_DATA, null).getVitaeLevel();
			if (entityVitae > 5 && !(entityLivingBase instanceof EntityPlayer))
			{
				entityLivingBase.getCapability(ModCapabilities.ENTITY_DATA, null).setVitaeLevel(entityVitae - 5);
				// Itemstack here is wrong? 
				this.addVitae(entityPlayer.getActiveItemStack(), 5);

				if (entityLivingBase instanceof EntityDeeeSyft && entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.DeeeSyft))
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.DeeeSyft, true);
			}
		}

		return super.itemInteractionForEntity(itemStack, entityPlayer, entityLivingBase, hand);
	}

	public int getStoredVitae(ItemStack itemStack)
	{
		return NBTHelper.getInt(itemStack, STORED_VITAE);
	}

	public boolean addVitae(ItemStack itemStack, int amount)
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
			list.add("Lantern is " + Math.round(100.0 * NBTHelper.getInt(itemStack, STORED_VITAE) / ItemVitaeLantern.VITAE_CAPACITY) + "% full.");
			if (Reference.isDebug)
			{
				list.add("Actual vitae = " + this.getStoredVitae(itemStack) + ", max = " + VITAE_CAPACITY);
			}
		}
		else
		{
			list.add("I'm not sure how to use this.");
		}
	}

	/**
	 * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is placed as a Block (mostly used with
	 * ItemBlocks).
	 */
	@Override
	public int getMetadata(ItemStack itemStack)
	{
		int storedVitae = NBTHelper.getInt(itemStack, STORED_VITAE);
		return storedVitae < 5 ? 0 : storedVitae < 100 ? 1 : storedVitae < 200 ? 2 : storedVitae < 290 ? 3 : 4;
	}

	/**
	 * Determines if the durability bar should be rendered for this item. Defaults to vanilla stack.isDamaged behavior. But modders can use this for
	 * any data they wish.
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
