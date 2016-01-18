/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethod;

import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.entity.player.EntityPlayer;

public class Projectile extends DeliveryMethod
{
	@Override
	public double getCost()
	{
		return 0;
	}

	@Override
	public void fireDeliveryMethod(DeliveryMethod previous, Spell callback, int spellStageIndex)
	{
	}

	@Override
	public void fireDeliveryMethod(EntityPlayer source, Spell callback, int spellStageIndex)
	{
		EntitySpellProjectile projectile = new EntitySpellProjectile(source.worldObj, source);
		projectile.setSpellSource(callback);
		projectile.setSpellStageIndex(spellStageIndex);
	}
}
