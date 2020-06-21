package com.davidm1a2.afraidofthedark.common.capabilities.player.spell

import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.entity.player.EntityPlayer
import java.util.*

/**
 * An interface that is a base for AOTD player spell capabilities
 */
interface IAOTDPlayerSpellManager {
    /**
     * Adds a spell to the list if it doesnt exist, or updates the spell if the ID already exists
     *
     * @param spell The spell to add or update
     */
    fun addOrUpdateSpell(spell: Spell)

    /**
     * Deletes a spell from the list
     *
     * @param spell The spell to delete from the list
     */
    fun deleteSpell(spell: Spell)

    /**
     * Clears all spells and keybinds stored
     */
    fun clearSpells()

    /**
     * @return An unmodifiable collection of spells that were added
     */
    fun getSpells(): List<Spell>

    /**
     * Adds a keybinding to a given spell
     *
     * @param key The key to bind to the spell
     * @param spell The spell to fire when the key is pressed
     */
    fun keybindSpell(key: String, spell: Spell)

    /**
     * Removes a keybinding to a given spell
     *
     * @param spell The spell to unbind
     */
    fun unbindSpell(spell: Spell)

    /**
     * Tests if a key is registered as a keybinding
     *
     * @param key The key to test
     * @return True if the key maps to a spell, false otherwise
     */
    fun keybindExists(key: String): Boolean

    /**
     * Gets the keybinding a spell is bound to or null if it is not bound to any
     *
     * @param spell The spell to grab the keybinding for
     * @return The keybinding a spell is bound to or null if it is unbound
     */
    fun getKeybindingForSpell(spell: Spell): String?

    /**
     * Gets the spell associated with a given keybinding or null if it does not exist
     *
     * @param key The keybinding to lookup spells for
     * @return A spell that this key is bound to or null if it does not exist
     */
    fun getSpellForKeybinding(key: String): Spell?

    /**
     * Gets a spell by it's ID
     *
     * @param spellId The spell's ID
     * @return The spell that has that ID, or null if it doesn't exist
     */
    fun getSpellById(spellId: UUID): Spell?

    /**
     * Synchronizes the spell manager between server and client, can be sent
     * from any side. This will sync all known spells and keybindings
     *
     * @param entityPlayer The player to sync the spell manager to
     */
    fun syncAll(entityPlayer: EntityPlayer)

    /**
     * Synchronizes a specific spell between server and client, can be sent from
     * any side. This will only send the spell and keybinding information for a single spell
     */
    fun sync(entityPlayer: EntityPlayer, spell: Spell)
}