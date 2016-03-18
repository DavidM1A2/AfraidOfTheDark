package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponent;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.nbt.NBTTagCompound;

public interface IPowerSource extends ISpellComponent
{
	/**
	 * 
	 * @param toCast
	 *            the spell to attempt to cast
	 * @return true if the power was consumed to cast the spell, false otherwise
	 */
	public abstract boolean attemptToCast(Spell toCast);

	/**
	 * 
	 * @return A String representing the chat message to be displayed to a player if they do not have enough energy to cast a given spell.
	 */
	public abstract String notEnoughEnergyMsg();

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
}
