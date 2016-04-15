package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponent;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.nbt.NBTTagCompound;

public interface IDeliveryMethod extends ISpellComponent
{
	/**
	 * 
	 * @return cost of the spell
	 */
	public abstract double getCost();

	/**
	 * 
	 * @return The multiplier to multiply effect costs by.
	 */
	public abstract double getStageMultiplier();

	/**
	 * 
	 * @param previous
	 *            The previous entity spell to this one
	 * @param spellStageIndex
	 *            The current index into the spell's array of stages
	 * @return An entity spell list ready to be instantiated into the mc world
	 */
	public abstract EntitySpell[] createSpellEntity(EntitySpell previous, int spellStageIndex);

	/**
	 * 
	 * @param callback
	 *            The spell that's calling the projectile to be made. This means this projectile is the first delivery method
	 * @return An entity spell list ready to be instantiated into the mc world
	 */
	public abstract EntitySpell[] createSpellEntity(Spell callback);

	/**
	 * 
	 * @param compound
	 *            The compound to write the current power source's data to
	 */
	public abstract void writeToNBT(NBTTagCompound compound);

	/**
	 * 
	 * @param compound
	 *            The compound to read the current power source's data from
	 */
	public abstract void readFromNBT(NBTTagCompound compound);

	/**
	 * 
	 * @return The delivery method enum constant representing this delivery method class
	 */
	@Override
	public abstract DeliveryMethods getType();
}
