package com.davidm1a2.afraidofthedark.common.capabilities.player.spell

import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.entity.player.PlayerEntity
import java.util.*

/**
 * An interface that is a base for AOTD player spell capabilities
 */
interface IPlayerSpellManager {
    /**
     * Creates a new spell in the spell manager
     *
     * @param spell The spell to add
     */
    fun createSpell(spell: Spell)

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
     * @param spell The spell to fire when the key is pressed
     * @param key The key to bind to the spell
     */
    fun keybindSpell(spell: Spell, key: String)

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
     * Creates/Updates an existing spell with a given network ID with a new spell definition and keybind. This should only be
     * called when receiving a spell related packet. If the given network ID is present, the spell with that ID is updated. If
     * it is not present, the spell is created.
     *
     * @param spell The new spell
     * @param networkId The spell's network ID
     * @param keybinding The spell's keybinding
     */
    fun updateSpellFromNetwork(spell: Spell, networkId: UUID, keybinding: String?)

    /**
     * Synchronizes the spell manager between server and client, can be sent
     * from any side. This will sync all known spells and keybindings
     *
     * @param entityPlayer The player to sync the spell manager to
     */
    fun syncAll(entityPlayer: PlayerEntity)

    /**
     * Synchronizes a specific spell between server and client, can be sent from
     * any side. This will only send the spell and keybinding information for a single spell
     */
    fun sync(entityPlayer: PlayerEntity, spell: Spell)
}