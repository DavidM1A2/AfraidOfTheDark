package com.davidm1a2.afraidofthedark.common.capabilities.player.spell

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.network.packets.capability.ClearSpellsPacket
import com.davidm1a2.afraidofthedark.common.network.packets.capability.SpellPacket
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.google.common.collect.HashBiMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * Default implementation of the AOTD spell manager
 */
class PlayerSpellManager : IPlayerSpellManager {
    private val spellNetworkIdMapping = HashBiMap.create<Spell, UUID>()
    private val spellKeybindingMapping = HashBiMap.create<Spell, String>()

    override fun createSpell(spell: Spell) {
        if (spellNetworkIdMapping.containsKey(spell)) {
            LOG.warn("Can't create the same spell twice")
        } else {
            spellNetworkIdMapping[spell] = UUID.randomUUID()
            // This shouldn't be needed, but just in case :)
            spellKeybindingMapping.remove(spell)
        }
    }

    override fun deleteSpell(spell: Spell) {
        spellNetworkIdMapping.remove(spell)
        spellKeybindingMapping.remove(spell)
    }

    override fun clearSpells() {
        spellNetworkIdMapping.clear()
        spellKeybindingMapping.clear()
    }

    override fun getSpells(): List<Spell> {
        return spellNetworkIdMapping.keys.toList()
    }

    override fun keybindSpell(spell: Spell, key: String) {
        if (spellNetworkIdMapping.containsKey(spell)) {
            spellKeybindingMapping.forcePut(spell, key)
        } else {
            LOG.error("Cannot keybind a spell that isn't registered!")
        }
    }

    override fun unbindSpell(spell: Spell) {
        spellKeybindingMapping.remove(spell)
    }

    override fun keybindExists(key: String): Boolean {
        return spellKeybindingMapping.inverse().containsKey(key)
    }

    override fun getKeybindingForSpell(spell: Spell): String? {
        return spellKeybindingMapping[spell]
    }

    override fun getSpellForKeybinding(key: String): Spell? {
        return spellKeybindingMapping.inverse()[key]
    }

    override fun updateSpellFromNetwork(spell: Spell, networkId: UUID, keybinding: String?) {
        spellNetworkIdMapping.forcePut(spell, networkId)
        if (keybinding != null) {
            spellKeybindingMapping.forcePut(spell, keybinding)
        } else {
            spellKeybindingMapping.remove(spell)
        }
    }

    override fun syncAll(entityPlayer: PlayerEntity) {
        // Clear the existing spells first
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(ClearSpellsPacket(), entityPlayer as ServerPlayerEntity)
        } else {
            AfraidOfTheDark.packetHandler.sendToServer(ClearSpellsPacket())
        }
        // Go over each spell and sync them one by one. We do this to avoid a large memory overhead
        // of sending many spells in a single packet
        getSpells().forEach { sync(entityPlayer, it) }
    }

    override fun sync(entityPlayer: PlayerEntity, spell: Spell) {
        // Grab the keybind or null
        val keybinding = getKeybindingForSpell(spell)
        // Create the packet
        val syncSpellPacket = SpellPacket(spell, spellNetworkIdMapping[spell]!!, keybinding)
        // If we're on server side send the packet to the player, otherwise send it to the server
        if (isServerSide(entityPlayer)) {
            AfraidOfTheDark.packetHandler.sendTo(syncSpellPacket, entityPlayer as ServerPlayerEntity)
        } else {
            AfraidOfTheDark.packetHandler.sendToServer(syncSpellPacket)
        }
    }

    private fun isServerSide(entityPlayer: PlayerEntity): Boolean {
        return !entityPlayer.level.isClientSide
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}