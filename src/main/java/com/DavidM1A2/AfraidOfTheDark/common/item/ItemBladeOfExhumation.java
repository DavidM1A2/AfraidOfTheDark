/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDSword;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraft.entity.Entity;
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
		boolean result = super.onLeftClickEntity(itemStack, entityPlayer, entity);

		if (entity instanceof EntityEnchantedSkeleton && !entity.isDead)
		{
			entity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), Float.MAX_VALUE);
		}

		return result;
	}

}
