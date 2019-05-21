package com.DavidM1A2.afraidofthedark.common.capabilities.player.spell;

import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;

/**
 * An interface that is a base for AOTD player spell capabilities
 */
public interface IAOTDPlayerSpellManager
{
    /**
     * Adds a spell to the list if it doesnt exist, or updates the spell if the ID already exists
     *
     * @param spell The spell to add or update
     */
    void addOrUpdateSpell(Spell spell);

    /**
     * Deletes a spell from the list
     *
     * @param spell The spell to delete from the list
     */
    void deleteSpell(Spell spell);

    /**
     * @return An unmodifiable collection of spells that were added
     */
    Collection<Spell> getSpells();

    /**
     * Adds a keybinding to a given spell
     *
     * @param key The key to bind to the spell
     * @param spell The spell to fire when the key is pressed
     */
    void keybindSpell(String key, Spell spell);

    /**
     * Tests if a key is registered as a keybinding
     *
     * @param key The key to test
     * @return True if the key maps to a spell, false otherwise
     */
    boolean keybindExists(String key);

    /**
     * Gets the keybinding a spell is bound to or null if it is not bound to any
     *
     * @param spell The spell to grab the keybinding for
     * @return The keybinding a spell is bound to or null if it is unbound
     */
    String getKeybindingForSpell(Spell spell);

    /**
     * Gets the spell associated with a given keybinding or null if it does not exist
     *
     * @param key The keybinding to lookup spells for
     * @return A spell that this key is bound to or null if it does not exist
     */
    Spell getSpellForKeybinding(String key);

    /**
     * Synchronizes the spell manager between server and client, can be sent
     * from any side. This will sync all known spells and keybindings
     *
     * @param entityPlayer The player to sync the spell manager to
     */
    void syncAll(EntityPlayer entityPlayer);

    /**
     * Synchronizes a specific spell between server and client, can be sent from
     * any side. This will only send the spell and keybinding information for a single spell
     */
    void sync(EntityPlayer entityPlayer, Spell spell);
}
