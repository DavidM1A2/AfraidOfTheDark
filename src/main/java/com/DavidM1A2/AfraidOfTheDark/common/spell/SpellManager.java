/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SpellManager implements Serializable
{
	Map<String, Spell> spells = new HashMap<String, Spell>();

	public void addSpell(Spell spell)
	{
		this.spells.put(spell.getName(), spell);
	}

	public void removeSpell(Spell spell)
	{
		this.spells.remove(spell.getName());
	}

	public void removeSpell(String name)
	{
		this.spells.remove(name);
	}

	public Spell getSpellFromName(String name)
	{
		return this.spells.get(name);
	}
}
