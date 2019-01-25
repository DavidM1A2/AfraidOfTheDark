package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItem;
import com.DavidM1A2.afraidofthedark.common.utility.NBTHelper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Class representing the crossbow item
 */
public class ItemCrossbow extends AOTDItem
{
	// Store the reload time of the crossbow in ticks
	private static final int RELOAD_TIME = 50;

	// Strings used as keys by NBT
	private static final String NBT_BOLT_TYPE = "bolt_type";

	/**
	 * Constructor just sets the item name and ensures it can't stack
	 */
	public ItemCrossbow()
	{
		super("crossbow");
		this.setMaxStackSize(1);
	}

	/**
	 * The metadata of the item is just it's damage value in our case
	 *
	 * @param damage The damage of the crossbow which represents its charge state
	 * @return The metadata value of the item
	 */
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	/**
	 * Fires a bolt from the crossbow into the world
	 *
	 * @param entityPlayer The player shooting the bow
	 * @param world The world the bow is being shot in
	 * @param itemStack The bow item stack
	 */
	private void fireBolt(EntityPlayer entityPlayer, World world, ItemStack itemStack)
	{
		world.playSound(null, entityPlayer.getPosition(), ModSounds.CROSSBOW_FIRE, SoundCategory.PLAYERS, 0.5f, world.rand.nextFloat() * 0.4f + 0.8f);
		// Instantiate bolt!
	}

	/**
	 * Called when a crossbow item is instantiated, sets the bow's mode to default
	 *
	 * @param stack The crossbow itemstack
	 * @param worldIn The world the crossbow is in
	 * @param playerIn The player that created the bow
	 */
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		NBTHelper.setInteger(stack, NBT_BOLT_TYPE, 0);
	}

	/**
	 * Adds a tooltop to the crossbow
	 *
	 * @param stack The item stack to tooltip
	 * @param worldIn The world the item is in
	 * @param tooltip The tooltip to add to
	 * @param flagIn True if show advanced info is on, false otherwise
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Shift & Right click to change crossbow bolt type.");
		tooltip.add("Bow will fire: " + " bolts.");
	}

	/**
	 * Returns the amount of time the item can be in use
	 *
	 * @param stack The itemstack in question
 \	 * @return An integer representing the reload time of the bow
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return RELOAD_TIME;
	}
}
