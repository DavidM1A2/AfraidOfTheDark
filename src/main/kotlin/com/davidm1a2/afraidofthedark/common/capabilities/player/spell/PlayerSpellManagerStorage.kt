package com.davidm1a2.afraidofthedark.common.capabilities.player.spell

import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.util.Constants
import org.apache.logging.log4j.LogManager

/**
 * Default storage implementation for the AOTD spell manager
 */
class PlayerSpellManagerStorage : IStorage<IPlayerSpellManager> {
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(
        capability: Capability<IPlayerSpellManager>,
        instance: IPlayerSpellManager,
        side: Direction?
    ): INBT {
        // Create a compound to write
        val compound = CompoundNBT()
        // Create a list of nbt spells
        val spellsNBT = ListNBT()

        // Write each spell to NBT
        for (spell in instance.getSpells()) {
            // Write the spell to NBT
            spellsNBT.add(spell.serializeNBT())
        }

        // Set the spell list into the compound
        compound.put(NBT_SPELLS_LIST, spellsNBT)

        // Go over each keybind and store it off
        val keybindingsNBT = ListNBT()
        // Go over every spell that has a keybinding
        for (spell in instance.getSpells()) {
            // Grab the keybinding for the spell, test if it's valid
            val keybinding = instance.getKeybindingForSpell(spell)
            if (keybinding != null) {
                // Store the spell UUID, keybind pair
                val keybindCompound = CompoundNBT()
                keybindCompound.put(NBT_KEYBIND_SPELL_UUID, NBTUtil.createUUID(spell.id))
                keybindCompound.putString(NBT_KEYBIND, keybinding)
                keybindingsNBT.add(keybindCompound)
            }
        }
        // Set the spell keybinds into the compound
        compound.put(NBT_KEYBINDS_LIST, keybindingsNBT)
        return compound
    }

    /**
     * Called to read the NBTTagCompound into a capability
     *
     * @param capability The capability that is being read
     * @param instance   The instance to of the capability to read
     * @param side       ignored
     * @param nbt        An NBTTagCompound that contains all info about the capability
     */
    override fun readNBT(
        capability: Capability<IPlayerSpellManager>,
        instance: IPlayerSpellManager,
        side: Direction?,
        nbt: INBT
    ) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is CompoundNBT) {
            // Grab the list of spells
            val spellsNBT = nbt.getList(NBT_SPELLS_LIST, Constants.NBT.TAG_COMPOUND)
            // Go over each spell in the list
            for (i in 0 until spellsNBT.size) {
                // Grab the compound for the spell
                val spellNBT = spellsNBT.getCompound(i)
                // Create a new spell instance, and read in the state from NBT
                val spell = Spell(spellNBT)
                // Add the spell to the list
                instance.addOrUpdateSpell(spell)
            }
            // A utility temp map of uuid -> spell for use in determining keybinds in O(1) for extra memory usage
            val idToSpell = instance.getSpells().associateBy { it.id }

            // Restore the keybindings
            val keybindingsNBT = nbt.getList(NBT_KEYBINDS_LIST, Constants.NBT.TAG_COMPOUND)
            // Go over each compound in the list
            for (i in 0 until keybindingsNBT.size) {
                // Grab the compound for the keybinding
                val keybindingNBT = keybindingsNBT.getCompound(i)
                // Grab the key and spell UUID
                val spellUUID = NBTUtil.loadUUID(keybindingNBT.get(NBT_KEYBIND_SPELL_UUID)!!)
                val keybind = keybindingNBT.getString(NBT_KEYBIND)
                // Keybind the key to the spell
                instance.keybindSpell(keybind, idToSpell[spellUUID]!!)
            }
        } else {
            logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // The spell list
        private const val NBT_SPELLS_LIST = "spells"

        // The keybinds list
        private const val NBT_KEYBINDS_LIST = "keybinds"

        // Two NBT fields, one for the keybind value and one for the keybind spell UUID used as a pair
        private const val NBT_KEYBIND = "keybind"
        private const val NBT_KEYBIND_SPELL_UUID = "keybind_spell_uuid"
    }
}