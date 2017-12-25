/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.entities.ICanTakeSilverDamage;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDChargableSword;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDamageSources;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDToolMaterials;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStarMetalKhopesh extends AOTDChargableSword
{
	private static final int HITRANGE = 5;

	public ItemStarMetalKhopesh()
	{
		super(AOTDToolMaterials.StarMetal.getToolMaterial(), "star_metal_khopesh");
		this.setRegistryName("star_metal_khopesh");
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
	public void addInformation(final ItemStack stack, final EntityPlayer entityPlayer, final List<String> tooltip, final boolean advanced)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.StarMetal))
		{
			tooltip.add("Magical items will never break.");
			tooltip.add("Right click to use an AOE knockback and");
			tooltip.add("damage attack when charged to 100%");
		}
		else
		{
			tooltip.add("I'm not sure how to use this.");
		}
	}

	// When left clicking attack from silver weapon damage
	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity)
	{
		if (player.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.StarMetal))
		{
			if (entity instanceof ICanTakeSilverDamage)
			{
				if (player.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD())
				{
					entity.attackEntityFrom(AOTDDamageSources.causeSilverDamage(player), 10F);
				}
			}
			return super.onLeftClickEntity(stack, player, entity);
		}
		else
		{
			entity.attackEntityFrom(DamageSource.causePlayerDamage(player), this.getDamageVsEntity());
			return true;
		}
	}

	@Override
	public int percentChargePerAttack()
	{
		return 10;
	}

	@Override
	public void performChargeAttack(ItemStack itemStack, World world, final EntityPlayer entityPlayer)
	{
		List entityList = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().expand(HITRANGE, HITRANGE, HITRANGE));
		for (Object entityObject : entityList)
		{
			if (entityObject instanceof EntityPlayer || entityObject instanceof EntityLiving)
			{
				EntityLivingBase entity = (EntityLivingBase) entityObject;
				double knockbackStrength = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(19), itemStack) + 2;

				double motionX = entityPlayer.getPosition().getX() - entity.getPosition().getX();
				double motionZ = entityPlayer.getPosition().getZ() - entity.getPosition().getZ();

				motionX = motionX >= 0 ? 1 : -1;
				motionZ = motionZ >= 0 ? 1 : -1;

				double hypotenuse = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);

				int sharpnessDamage = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(16), itemStack);

				entity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), AOTDToolMaterials.StarMetal.getToolMaterial().getDamageVsEntity() + 4.0F + sharpnessDamage * 1.5F);
				entity.addVelocity(-motionX * knockbackStrength * 0.6000000238418579D / hypotenuse, 0.1D, -motionZ * knockbackStrength * 0.6000000238418579D / hypotenuse);
			}
		}

		final int DEGREES_PER_ROTATION = 30;
		final int TIME_BETWEEN_ROTATIONS = 20;
		for (int i = 0; i < 360 / DEGREES_PER_ROTATION; i++)
		{
			AfraidOfTheDark.instance.getDelayedExecutor().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					entityPlayer.rotationYaw = entityPlayer.rotationYaw + DEGREES_PER_ROTATION;
				}
			}, TIME_BETWEEN_ROTATIONS * i, TimeUnit.MILLISECONDS);
		}
	}
}
