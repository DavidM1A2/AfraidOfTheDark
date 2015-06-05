/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.common.threads.TemporaryInvincibility;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

public class ItemStarMetalStaff extends AOTDItem implements IHasCooldown
{
	private static final int MAX_TROLL_POLE_TIME_IN_TICKS = 60;
	private double cooldownRemaining = 0;

	public ItemStarMetalStaff()
	{
		super();
		this.setUnlocalizedName("starMetalStaff");
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (cooldownRemaining == 0)
		{
			if (!entityPlayer.capabilities.isCreativeMode)
			{
				new TemporaryInvincibility(Utility.ticksToMilliseconds(MAX_TROLL_POLE_TIME_IN_TICKS), entityPlayer);
			}
			entityPlayer.addVelocity(0, .5, 0);
			entityPlayer.setItemInUse(itemStack, ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS);
		}
		else
		{
			if (world.isRemote)
			{
				if (entityPlayer.getItemInUse() != itemStack)
				{
					entityPlayer.addChatMessage(new ChatComponentText(this.cooldownRemaining != 0 ? ("Cooldown remaining: " + ((int) this.cooldownRemaining / 20 + 1) + " second" + (this.cooldownRemaining / 20 == 0.0 ? "." : "s.")) : "Ready to Use"));
				}
			}
		}

		return super.onItemRightClick(itemStack, world, entityPlayer);
	}

	/**
	 * Called each tick while using an item.
	 * 
	 * @param stack
	 *            The Item being used
	 * @param player
	 *            The Player using the item
	 * @param count
	 *            The amount of time in tick the item has been used for continuously
	 */
	@Override
	public void onUsingTick(final ItemStack stack, final EntityPlayer entityPlayer, int count)
	{
		count = ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS - count;
		if (count == 1)
		{
			cooldownRemaining = this.getItemCooldownInTicks();
			entityPlayer.fallDistance = 0.0f;
		}
		else if (count >= 3)
		{
			if (entityPlayer.worldObj.isRemote)
			{
				entityPlayer.setVelocity(0, 0, 0);
			}
		}
		if (count == (ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS - 1))
		{
			entityPlayer.stopUsingItem();
		}
	}

	/**
	 * Called when the player stops using an Item (stops holding the right mouse button).
	 *
	 * @param timeLeft
	 *            The amount of ticks left before the using would have been complete
	 */
	@Override
	public void onPlayerStoppedUsing(final ItemStack stack, final World worldIn, final EntityPlayer entityPlayer, final int timeLeft)
	{
		if (!entityPlayer.capabilities.isCreativeMode)
		{
			entityPlayer.capabilities.disableDamage = false;
		}
		if (timeLeft < 5)
		{
			List entityList = worldIn.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().expand(10, 10, 10));
			for (Object entityObject : entityList)
			{
				if (entityObject instanceof EntityPlayer || entityObject instanceof EntityLiving)
				{
					EntityLivingBase entity = (EntityLivingBase) entityObject;

					double knockbackStrength = 2;

					double motionX = entityPlayer.getPosition().getX() - entity.getPosition().getX();
					double motionZ = entityPlayer.getPosition().getZ() - entity.getPosition().getZ();

					double hypotenuse = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

					entity.addVelocity(-motionX * knockbackStrength * 0.6000000238418579D / hypotenuse, 0.1D, -motionZ * knockbackStrength * 0.6000000238418579D / hypotenuse);
				}
			}
		}
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		return super.onItemUseFinish(stack, worldIn, playerIn);
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack)
	{
		return ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 *
	 * @param tooltip
	 *            All lines to display in the Item's tooltip. This is a List of Strings.
	 * @param advanced
	 *            Whether the setting "Advanced tooltips" is enabled
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced)
	{
		tooltip.add("Right click for temporary invincibility");
		tooltip.add("followed by an AOE knockback.");
		tooltip.add("Max cooldown: " + this.getItemCooldownInTicks() / 20 + " seconds.");
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
	 */
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (this.cooldownRemaining > 0)
		{
			cooldownRemaining = cooldownRemaining - 0.5;
		}
	}

	@Override
	public int getItemCooldownInTicks()
	{
		return 1200;
	}
}