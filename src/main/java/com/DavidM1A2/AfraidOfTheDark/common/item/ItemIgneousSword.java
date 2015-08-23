/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.entities.ICanTakeSilverDamage;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDChargableSword;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

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
		super(Constants.AOTDToolMaterials.igneousTool, "igneousSword");
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity)
	{
		if (Research.isResearched(player, ResearchTypes.Igneous))
		{
			if (entity != null)
			{
				entity.setFire(5 + EnchantmentHelper.getEnchantmentLevel(20, stack) * 10);

				if (entity instanceof ICanTakeSilverDamage)
				{
					entity.attackEntityFrom(Constants.AOTDDamageSources.getSilverDamage(player), 10F);
				}
			}

			return super.onLeftClickEntity(stack, player, entity);
		}
		else
		{
			entity.attackEntityFrom(DamageSource.causePlayerDamage(player), this.func_150931_i());
			return true;
		}
	}

	// The journal shows who it is soulbound to
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List tooltip, final boolean p_77624_4_)
	{
		if (Research.isResearched(entityPlayer, ResearchTypes.Igneous))
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
		return 5;
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
