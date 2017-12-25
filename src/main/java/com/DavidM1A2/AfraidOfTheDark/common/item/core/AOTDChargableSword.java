/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.core;

import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Set;

public abstract class AOTDChargableSword extends AOTDSword
{
	private final String itemName;

	public AOTDChargableSword(ToolMaterial material, String itemName)
	{
		super(material);
		this.itemName = itemName;
		this.maxStackSize = 1;
		this.setUnlocalizedName(itemName);
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
		if (target instanceof EntityPlayer || target instanceof EntityLiving)
		{
			if (NBTHelper.getInt(itemStack, "charge") < 100)
			{
				NBTHelper.setInteger(itemStack, "charge", NBTHelper.getInt(itemStack, "charge") + this.percentChargePerAttack());
			}
			if (NBTHelper.getInt(itemStack, "charge") > 100)
			{
				NBTHelper.setInteger(itemStack, "charge", 100);
			}
		}
		return true;
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param itemStack
	 *            The current ItemStack
	 * @return 1.0 for 100% 0 for 0%
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack itemStack)
	{
		return 1.0 - (double) NBTHelper.getInt(itemStack, "charge") / (double) 100;
	}

	@Override
	public boolean isDamageable()
	{
		return false;
	}

	@Override
	public boolean showDurabilityBar(ItemStack itemStack)
	{
		return true;
	}

	/**
	 * This used to be 'display damage' but its really just 'aux' data in the ItemStack, usually shares the same variable as damage.
	 * 
	 * @param itemStack
	 * @return
	 */
	@Override
	public int getMetadata(ItemStack itemStack)
	{
		return NBTHelper.getInt(itemStack, "charge") == 100 ? 1 : 0;
	}

	/**
	 * Can't block with this sword
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.NONE;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer entityPlayer, final EnumHand hand)
	{
		ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
		if (NBTHelper.getInt(itemStack, "charge") >= 100)
		{
			NBTHelper.setInteger(itemStack, "charge", 0);
			this.performChargeAttack(itemStack, world, entityPlayer);
		}
		else
		{
			if (!world.isRemote)
				entityPlayer.sendMessage(new TextComponentString("I'll need more energy to perform the ability."));
		}

		return ActionResult.<ItemStack> newResult(EnumActionResult.SUCCESS, itemStack);
	}

	public abstract int percentChargePerAttack();

	public abstract void performChargeAttack(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer);
}
