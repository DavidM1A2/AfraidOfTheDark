/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDSword;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class ItemBladeOfExhumation extends AOTDSword
{
	public ItemBladeOfExhumation()
	{
		super(Constants.AOTDToolMaterials.bladeOfExhumation);
		this.setUnlocalizedName("bladeOfExhumation");
	}

	/**
	 * Called when the player Left Clicks (attacks) an entity. Processed before damage is done, if return value is true further processing is canceled
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
	public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer entityPlayer, Entity entity)
	{
		if (itemStack.getItemDamage() == itemStack.getMaxDamage() - 1)
		{
			return true;
		}

		boolean result = super.onLeftClickEntity(itemStack, entityPlayer, entity);

		if (AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.BladeOfExhumation))
		{
			if (entity instanceof EntityEnchantedSkeleton && !entity.isDead)
			{
				entity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), Float.MAX_VALUE);
				this.hitEntity(itemStack, (EntityEnchantedSkeleton) entity, entityPlayer);
			}
		}

		return result;
	}

	/**
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise the damage on the stack.
	 * 
	 * @param target
	 *            The Entity being hit
	 * @param attacker
	 *            the attacking entity
	 */
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (itemStack.getItemDamage() == this.getMaxDamage() - 1)
		{
			return false;
		}
		else
		{
			if (itemStack.getItemDamage() == this.getMaxDamage() - 2)
			{
				attacker.playSound("random.break", 0.8F, 0.8F + attacker.worldObj.rand.nextFloat() * 0.4F);
			}
			return super.hitEntity(itemStack, target, attacker);
		}
	}

}
