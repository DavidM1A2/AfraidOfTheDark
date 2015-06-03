/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.threads.PlayerSpinning;
import com.DavidM1A2.AfraidOfTheDark.utility.NBTHelper;

public class ItemStarMetalKhopesh extends AOTDSword
{
	private static final int HITRANGE = 5;

	public ItemStarMetalKhopesh()
	{
		super(Refrence.starMetalTool);
		this.setUnlocalizedName("starMetalKhopesh");
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack
	 *            The current ItemStack
	 * @return 1.0 for 100% 0 for 0%
	 */
	public double getDurabilityForDisplay(ItemStack itemStack)
	{
		return 1.0 - (double) NBTHelper.getInt(itemStack, "charge") / (double) 100;
	}

	@Override
	public boolean showDurabilityBar(ItemStack itemStack)
	{
		return true;
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
				NBTHelper.setInteger(itemStack, "charge", NBTHelper.getInt(itemStack, "charge") + 5);
			}
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (!world.isRemote)
		{
			if (NBTHelper.getInt(itemStack, "charge") >= 100)
			{
				List entityList = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().expand(HITRANGE, HITRANGE, HITRANGE));
				for (Object entityObject : entityList)
				{
					if (entityObject instanceof EntityPlayer || entityObject instanceof EntityLiving)
					{
						EntityLivingBase entity = (EntityLivingBase) entityObject;
						double knockbackStrength = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, itemStack) + 2;

						double motionX = entityPlayer.getPosition().getX() - entity.getPosition().getX();
						double motionZ = entityPlayer.getPosition().getZ() - entity.getPosition().getZ();

						motionX = motionX >= 0 ? 1 : -1;
						motionZ = motionZ >= 0 ? 1 : -1;

						double hypotenuse = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

						entity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), Refrence.starMetalTool.getDamageVsEntity() + 4.0F);
						entity.addVelocity(-motionX * knockbackStrength * 0.6000000238418579D / hypotenuse, 0.1D, -motionZ * knockbackStrength * 0.6000000238418579D / hypotenuse);
					}
				}

				new PlayerSpinning((EntityPlayerMP) entityPlayer).start();

				NBTHelper.setInteger(itemStack, "charge", 0);
			}
			else
			{
				entityPlayer.addChatMessage(new ChatComponentText("Charge at " + NBTHelper.getInt(itemStack, "charge") + "%"));
			}
		}
		return super.onItemRightClick(itemStack, world, entityPlayer);
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
		tooltip.add("Right click to use an AOE knockback and");
		tooltip.add("damage attack when charged to 100%");
	}
}
