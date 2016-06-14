package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponent;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellHitInfo;

public interface IEffect extends ISpellComponent
{
	public abstract int getCost();

	public abstract void performEffect(SpellHitInfo hitInfo);

	/**
	 * 
	 * @return The ieffect enum constant representing this effect class
	 */
	@Override
	public abstract Effects getType();
}
