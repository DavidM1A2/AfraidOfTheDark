package com.davidm1a2.afraidofthedark.common.capabilities.player.spell

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.util.Constants
import java.util.*

/**
 * Default storage implementation for the AOTD spell manager
 */
class AOTDPlayerSpellManagerStorage : IStorage<IAOTDPlayerSpellManager>
{
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(
        capability: Capability<IAOTDPlayerSpellManager>,
        instance: IAOTDPlayerSpellManager,
        side: EnumFacing?
    ): NBTBase?
    {
        // Create a compound to write
        val compound = NBTTagCompound()
        // Create a list of nbt spells
        val spellsNBT = NBTTagList()

        // Write each spell to NBT
        for (spell in instance.getSpells())
        {
            // Write the spell to NBT
            spellsNBT.appendTag(spell.serializeNBT())
        }

        // Set the spell list into the compound
        compound.setTag(NBT_SPELLS_LIST, spellsNBT)

        // Go over each keybind and store it off
        val keybindingsNBT = NBTTagList()
        // Go over every spell that has a keybinding
        for (spell in instance.getSpells())
        {
            // Grab the keybinding for the spell, test if it's valid
            val keybinding = instance.getKeybindingForSpell(spell)
            if (keybinding != null)
            {
                // Store the spell UUID, keybind pair
                val keybindCompound = NBTTagCompound()
                keybindCompound.setTag(NBT_KEYBIND_SPELL_UUID, NBTUtil.createUUIDTag(spell.id))
                keybindCompound.setString(NBT_KEYBIND, keybinding)
                keybindingsNBT.appendTag(keybindCompound)
            }
        }
        // Set the spell keybinds into the compound
        compound.setTag(NBT_KEYBINDS_LIST, keybindingsNBT)
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
        capability: Capability<IAOTDPlayerSpellManager>,
        instance: IAOTDPlayerSpellManager,
        side: EnumFacing?,
        nbt: NBTBase
    )
    {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is NBTTagCompound)
        {
            // Grab the list of spells
            val spellsNBT = nbt.getTagList(NBT_SPELLS_LIST, Constants.NBT.TAG_COMPOUND)
            // Go over each spell in the list
            for (i in 0 until spellsNBT.tagCount())
            {
                // Grab the compound for the spell
                val spellNBT = spellsNBT.getCompoundTagAt(i)
                // Create a new spell instance, and read in the state from NBT
                val spell = Spell(spellNBT)
                // Add the spell to the list
                instance.addOrUpdateSpell(spell)
            }
            // A utility temp map of uuid -> spell for use in determining keybinds in O(1) for extra memory usage
            val idToSpell: Map<UUID, Spell> = instance.getSpells()
                .map { it.id to it }
                .toMap()

            // Restore the keybindings
            val keybindingsNBT = nbt.getTagList(NBT_KEYBINDS_LIST, Constants.NBT.TAG_COMPOUND)
            // Go over each compound in the list
            for (i in 0 until keybindingsNBT.tagCount())
            {
                // Grab the compound for the keybinding
                val keybindingNBT = keybindingsNBT.getCompoundTagAt(i)
                // Grab the key and spell UUID
                val spellUUID = NBTUtil.getUUIDFromTag(keybindingNBT.getCompoundTag(NBT_KEYBIND_SPELL_UUID))
                val keybind = keybindingNBT.getString(NBT_KEYBIND)
                // Keybind the key to the spell
                instance.keybindSpell(keybind, idToSpell[spellUUID]!!)
            }
        }
        else
        {
            AfraidOfTheDark.INSTANCE.logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object
    {
        // The spell list
        private const val NBT_SPELLS_LIST = "spells"
        // The keybinds list
        private const val NBT_KEYBINDS_LIST = "keybinds"
        // Two NBT fields, one for the keybind value and one for the keybind spell UUID used as a pair
        private const val NBT_KEYBIND = "keybind"
        private const val NBT_KEYBIND_SPELL_UUID = "keybind_spell_uuid"
    }
}