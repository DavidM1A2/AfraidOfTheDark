/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpellManager implements Serializable
{
	Map<UUID, Spell> spells = new HashMap<UUID, Spell>();

	public void addSpell(Spell spell)
	{
		this.spells.put(spell.getSpellUUID(), spell);
	}

	public void removeSpell(Spell spell)
	{
		this.spells.remove(spell.getSpellUUID());
	}
}
