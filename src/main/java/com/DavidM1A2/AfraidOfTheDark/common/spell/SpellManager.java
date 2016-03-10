/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.entity.player.EntityPlayer;

public class SpellManager implements Serializable
{
	private BiMap<Character, UUID> keyToSpell = HashBiMap.<Character, UUID> create();
	private BiMap<UUID, Spell> spells = HashBiMap.<UUID, Spell> create();

	public void addSpell(Spell spell)
	{
		this.spells.put(spell.getSpellUUID(), spell);
	}

	public void removeSpell(Spell spell)
	{
		this.spells.remove(spell.getSpellUUID());
	}

	public Collection<Spell> getSpellList()
	{
		return spells.values();
	}

	public void addKeybindingToSpell(char key, Spell spell)
	{
		this.keyToSpell.put(key, spell.getSpellUUID());
	}

	public void removeKeybindingToSpell(char key, Spell spell)
	{
		this.keyToSpell.remove(key, spell.getSpellUUID());
	}

	public void setAllSpellsOwners(EntityPlayer owner)
	{
		for (Spell spell : this.spells.values())
			spell.setSpellOwner(owner);
	}

	// Called server side to instantiate the spell
	public void keyPressed(int keyCode, char key)
	{
		if (this.doesKeyMapToSpell(key))
			this.spells.get(this.keyToSpell.get(key)).instantiateSpell();
	}

	public boolean doesKeyMapToSpell(char key)
	{
		return this.keyToSpell.containsKey(key) && this.spells.containsKey(this.keyToSpell.get(key));
	}

	public Character keyFromSpell(Spell spell)
	{
		UUID current = spells.inverse().get(spell);
		if (current != null)
		{
			Character key = keyToSpell.inverse().get(current);
			if (key != null)
			{
				return key;
			}
		}
		return null;
	}
}
