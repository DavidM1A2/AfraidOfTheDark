/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.common.threads.PlayerSpinning;

public class ItemStarMetalKhopesh extends AOTDChargableSword
{
	private static final int HITRANGE = 5;

	public ItemStarMetalKhopesh()
	{
		super(Refrence.starMetalTool, "starMetalKhopesh");
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
		tooltip.add("Magical items will never break.");
		tooltip.add("Right click to use an AOE knockback and");
		tooltip.add("damage attack when charged to 100%");
	}

	@Override
	public int percentChargePerAttack()
	{
		return 5;
	}

	@Override
	public void performChargeAttack(ItemStack itemStack, World world, EntityPlayer entityPlayer)
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
	}
}