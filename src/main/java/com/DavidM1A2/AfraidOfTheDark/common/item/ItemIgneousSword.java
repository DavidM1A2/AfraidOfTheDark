/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.entities.ICanTakeSilverDamage;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDChargableSword;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDamageSources;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDToolMaterials;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemIgneousSword extends AOTDChargableSword
{
	private static final int HIT_RANGE = 5;

	public ItemIgneousSword()
	{
		super(AOTDToolMaterials.Igneous.getToolMaterial(), "igneousSword");
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity)
	{
		if (player.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.Igneous))
		{
			if (entity != null)
			{
				entity.setFire(5 + EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(20), stack) * 10);

				if (entity instanceof ICanTakeSilverDamage)
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

	// The journal shows who it is soulbound to
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List tooltip, final boolean p_77624_4_)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.Igneous))
		{
			tooltip.add("Magical items will never break.");
			tooltip.add("Right click to use an AOE fire strike");
			tooltip.add("when charged to 100%");
		}
		else
		{
			tooltip.add("I'm not sure how to use this.");
		}
	}

	@Override
	public int percentChargePerAttack()
	{
		return 10;
	}

	@Override
	public void performChargeAttack(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		List<Entity> surroundingEntities = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().expand(HIT_RANGE, HIT_RANGE, HIT_RANGE));

		for (Entity entity : surroundingEntities)
		{
			if (entity instanceof EntityLivingBase)
			{
				entity.setFire(20);
			}
		}
	}
}
