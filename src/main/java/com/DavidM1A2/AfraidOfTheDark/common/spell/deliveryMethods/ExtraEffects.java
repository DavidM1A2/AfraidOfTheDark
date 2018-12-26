/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.nbt.NBTTagCompound;

public class ExtraEffects extends DeliveryMethod
{
	@Override
	public double getCost()
	{
		return 0;
	}

	@Override
	public double getStageMultiplier()
	{
		return 1.0;
	}

	@Override
	public EntitySpell[] createSpellEntity(EntitySpell previous, int spellStageIndex)
	{
		return new EntitySpell[0];
	}

	@Override
	public EntitySpell[] createSpellEntity(Spell callback)
	{
		return new EntitySpell[0];
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
	}

	@Override
	public DeliveryMethods getType()
	{
		return DeliveryMethods.ExtraEffects;
	}
}