package com.davidm1a2.afraidofthedark.common.capabilities.player.spell

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.packets.capabilityPackets.ClearSpellsPacketProcessor
import com.davidm1a2.afraidofthedark.common.packets.capabilityPackets.SpellPacket
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * Default implementation of the AOTD spell manager
 *
 * @property keybindToSpellId Key -> spell id is a bimap so we can answer queries both ways quickly
 * @property spellIdToSpell Use a linked hash map to keep order of spells
 */
class AOTDPlayerSpellManagerImpl : IAOTDPlayerSpellManager {
    private val keybindToSpellId: BiMap<String, UUID> = HashBiMap.create()
    private val spellIdToSpell: MutableMap<UUID, Spell> = linkedMapOf()

    /**
     * Adds a spell to the list if it doesnt exist, or updates the spell if the ID already exists
     *
     * @param spell The spell to add or update
     */
    override fun addOrUpdateSpell(spell: Spell) {
        spellIdToSpell[spell.id] = spell
    }

    /**
     * Deletes a spell from the list
     *
     * @param spell The spell to delete from the list
     */
    override fun deleteSpell(spell: Spell) {
        spellIdToSpell.remove(spell.id)
        // Make use of our bi-map to remove by "value"
        keybindToSpellId.inverse().remove(spell.id)
    }

    /**
     * Clears all spells and keybinds stored
     */
    override fun clearSpells() {
        spellIdToSpell.clear()
        keybindToSpellId.clear()
    }

    /**
     * @return An unmodifiable list of spells that were added
     */
    override fun getSpells(): List<Spell> {
        return spellIdToSpell.values.toList()
    }

    /**
     * Adds a keybinding to a given spell
     *
     * @param key The key to bind to the spell
     * @param spell The spell to fire when the key is pressed
     */
    override fun keybindSpell(key: String, spell: Spell) {
        // Test if we have a spell registered by the id first
        if (spellIdToSpell.containsKey(spell.id)) {
            keybindToSpellId.forcePut(key, spell.id)
        } else {
            logger.error("Cannot bind a spell that isn't registered!")
        }
    }

    /**
     * Removes a keybinding to a given spell
     *
     * @param spell The spell to unbind
     */
    override fun unbindSpell(spell: Spell) {
        // Remove the key bound to the spell
        keybindToSpellId.inverse().remove(spell.id)
    }

    /**
     * Tests if a key is registered as a keybinding
     *
     * @param key The key to test
     * @return True if the key maps to a spell, false otherwise
     */
    override fun keybindExists(key: String): Boolean {
        return keybindToSpellId.containsKey(key)
    }

    /**
     * Gets the keybinding a spell is bound to or null if it is not bound to any
     *
     * @param spell The spell to grab the keybinding for
     * @return The keybinding a spell is bound to or null if it is unbound
     */
    override fun getKeybindingForSpell(spell: Spell): String? {
        return keybindToSpellId.inverse().getOrDefault(spell.id, null)
    }

    /**
     * Gets the spell associated with a given keybinding or null if it does not exist
     *
     * @param key The keybinding to lookup spells for
     * @return A spell that this key is bound to or null if it does not exist
     */
    override fun getSpellForKeybinding(key: String): Spell? {
        // Get the spell's UUID for the keybinding
        val spellUUID = keybindToSpellId.getOrDefault(key, null)
        // If it's non-null get the spell for the id
        return if (spellUUID != null) {
            // Return the spell or null
            spellIdToSpell.getOrDefault(spellUUID, null)
        } else null
    }

    /**
     * Gets a spell by it's ID
     *
     * @param spellId The spell's ID
     * @return The spell that has that ID, or null if it doesn't exist
     */
    override fun getSpellById(spellId: UUID): Spell? {
        return spellIdToSpell.getOrDefault(spellId, null)
    }

    /**
     * Synchronizes the spell manager between server and client, can be sent
     * from any side. This will sync all known spells and keybindings
     *
     * @param entityPlayer The player to sync the spell manager to
     */
    override fun syncAll(entityPlayer: EntityPlayer) {
        // Clear the existing spells first
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(ClearSpellsPacketProcessor(), entityPlayer as EntityPlayerMP)
        } else {
            AfraidOfTheDark.packetHandler.sendToServer(ClearSpellsPacketProcessor())
        }
        // Go over each spell and sync them one by one. We do this to avoid a large memory overhead
        // of sending many spells in a single packet
        spellIdToSpell.values.forEach { sync(entityPlayer, it) }
    }

    /**
     * Synchronizes a specific spell between server and client, can be sent from
     * any side. This will only send the spell and keybinding information for a single spell
     */
    override fun sync(entityPlayer: EntityPlayer, spell: Spell) {
        // Grab the keybind or null
        val keybind = getKeybindingForSpell(spell)
        // Create the packet
        val syncSpellPacket = SpellPacket(spell, keybind)
        // If we're on server side send the packet to the player, otherwise send it to the server
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(syncSpellPacket, entityPlayer as EntityPlayerMP)
        } else {
            AfraidOfTheDark.packetHandler.sendToServer(syncSpellPacket)
        }
    }

    /**
     * Returns true if the player is on server side or false if not
     *
     * @param entityPlayer The player to test
     * @return true if the player is on server side or false if not
     */
    private fun isServerSide(entityPlayer: EntityPlayer): Boolean {
        return !entityPlayer.world.isRemote
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}