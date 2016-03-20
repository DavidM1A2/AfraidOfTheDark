/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NBTTagCompound;

public class SpellManager
{
	private BiMap<String, UUID> keyToSpellUUID = HashBiMap.<String, UUID> create();
	private BiMap<UUID, Spell> spells = HashBiMap.<UUID, Spell> create();

	public void writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound spellManager = new NBTTagCompound();

		// Encode Char to UUID bimap
		Iterator<Entry<String, UUID>> keyToSpellUUIDIterator = keyToSpellUUID.entrySet().iterator();
		spellManager.setInteger("numberKeyToSpellUUIDMappings", keyToSpellUUID.entrySet().size());
		int index = 0;
		while (keyToSpellUUIDIterator.hasNext())
		{
			Entry<String, UUID> entry = keyToSpellUUIDIterator.next();
			String character = entry.getKey();
			UUID uuid = entry.getValue();
			spellManager.setString("Character" + index, character);
			spellManager.setLong("UUIDMost" + index, uuid.getMostSignificantBits());
			spellManager.setLong("UUIDLeast" + index, uuid.getLeastSignificantBits());
			index = index + 1;
		}

		// Encode UUID to spell map
		Iterator<Entry<UUID, Spell>> uuidToSpellIterator = spells.entrySet().iterator();
		spellManager.setInteger("numberUUIDToSpellMappings", spells.entrySet().size());
		index = 0;
		while (uuidToSpellIterator.hasNext())
		{
			Entry<UUID, Spell> entry = uuidToSpellIterator.next();
			UUID uuid = entry.getKey();
			Spell spell = entry.getValue();
			spellManager.setLong("UUID2Most" + index, uuid.getMostSignificantBits());
			spellManager.setLong("UUID2Least" + index, uuid.getLeastSignificantBits());
			NBTTagCompound spellNBT = new NBTTagCompound();
			spell.writeToNBT(spellNBT);
			spellManager.setTag("spell" + index, spellNBT);
			index = index + 1;
		}

		compound.setTag("spellManager", spellManager);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		NBTTagCompound spellManager = compound.getCompoundTag("spellManager");

		this.keyToSpellUUID.clear();
		for (int i = 0; i < spellManager.getInteger("numberKeyToSpellUUIDMappings"); i++)
		{
			String character = spellManager.getString("Character" + i);
			Long mostSignificant = spellManager.getLong("UUIDMost" + i);
			Long leastSignificant = spellManager.getLong("UUIDLeast" + i);
			UUID uuid = new UUID(mostSignificant, leastSignificant);
			this.keyToSpellUUID.put(character, uuid);
		}

		this.spells.clear();
		for (int i = 0; i < spellManager.getInteger("numberUUIDToSpellMappings"); i++)
		{
			Long mostSignificant = spellManager.getLong("UUID2Most" + i);
			Long leastSignificant = spellManager.getLong("UUID2Least" + i);
			UUID uuid = new UUID(mostSignificant, leastSignificant);
			NBTTagCompound spellNBT = spellManager.getCompoundTag("spell" + i);
			Spell spell = new Spell(spellNBT);
			this.spells.put(uuid, spell);
		}
	}

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

	public void setKeybindingToSpell(String key, Spell spell)
	{
		this.keyToSpellUUID.forcePut(key, spell.getSpellUUID());
	}

	// Called server side to instantiate the spell
	public void keyPressed(int keyCode, char key)
	{
		String keyName = Keyboard.getKeyName(keyCode);
		if (keyName != null)
		{
			if (this.doesKeyMapToSpell(keyName))
				this.spells.get(this.keyToSpellUUID.get(keyName)).instantiateSpell();
		}
	}

	public boolean doesKeyMapToSpell(String key)
	{
		return this.keyToSpellUUID.containsKey(key) && this.spells.containsKey(this.keyToSpellUUID.get(key));
	}

	public String keyFromSpell(Spell spell)
	{
		UUID current = spells.inverse().get(spell);
		if (current != null)
		{
			String key = keyToSpellUUID.inverse().get(current);
			if (key != null)
			{
				return key;
			}
		}
		return null;
	}
}
