package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponent;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

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
	 * @return The ieffect enum constant representing this effect class
	 */
	@Override
	public abstract PowerSources getType();
}
