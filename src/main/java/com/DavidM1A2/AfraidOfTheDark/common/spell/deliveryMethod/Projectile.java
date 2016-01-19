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
		if (previous == null)
		{
			EntityPlayer spellOwner = callback.getSpellOwner();
			EntitySpellProjectile projectile = new EntitySpellProjectile(spellOwner.worldObj, spellOwner, spellOwner.posX, spellOwner.posY + 0.8d, spellOwner.posZ, spellOwner.getLookVec().xCoord, spellOwner.getLookVec().yCoord, spellOwner.getLookVec().zCoord, true);
			projectile.setSpellSource(callback);
			projectile.setSpellStageIndex(spellStageIndex);
		}
		else if (previous instanceof Projectile)
		{
			EntityPlayer spellOwner = callback.getSpellOwner();
			EntitySpellProjectile projectile = new EntitySpellProjectile(spellOwner.worldObj, spellOwner, spellOwner.posX, spellOwner.posY + 0.8d, spellOwner.posZ, spellOwner.getLookVec().xCoord, spellOwner.getLookVec().yCoord, spellOwner.getLookVec().zCoord, false);
			projectile.setSpellSource(callback);
			projectile.setSpellStageIndex(spellStageIndex);
		}
	}
}
