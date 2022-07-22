package com.davidm1a2.afraidofthedark.common.capabilities.player.spell

import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.util.Constants
import org.apache.logging.log4j.LogManager

/**
 * Default storage implementation for the AOTD spell manager
 */
class PlayerSpellManagerStorage : IStorage<IPlayerSpellManager> {
    override fun writeNBT(
        capability: Capability<IPlayerSpellManager>,
        instance: IPlayerSpellManager,
        side: Direction?
    ): INBT {
        val compound = CompoundNBT()

        val spellListNbt = ListNBT()
        for (spell in instance.getSpells()) {
            val spellListEntryNbt = CompoundNBT()

            spellListEntryNbt.put(NBT_SPELL, spell.serializeNBT())
            val keybinding = instance.getKeybindingForSpell(spell)
            if (keybinding != null) {
                spellListEntryNbt.putString(NBT_KEYBIND, keybinding)
            }

            spellListNbt.add(spellListEntryNbt)
        }
        compound.put(NBT_SPELL_LIST, spellListNbt)

        return compound
    }

    override fun readNBT(
        capability: Capability<IPlayerSpellManager>,
        instance: IPlayerSpellManager,
        side: Direction?,
        nbt: INBT
    ) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is CompoundNBT) {
            val spellListNbt = nbt.getList(NBT_SPELL_LIST, Constants.NBT.TAG_COMPOUND)
            for (i in 0 until spellListNbt.size) {
                val spellListEntryNbt = spellListNbt.getCompound(i)
                val spellNbt = spellListEntryNbt.getCompound(NBT_SPELL)
                val spell = Spell(spellNbt)
                instance.createSpell(spell)

                if (spellListEntryNbt.contains(NBT_KEYBIND)) {
                    val keybinding = spellListEntryNbt.getString(NBT_KEYBIND)
                    instance.keybindSpell(spell, keybinding)
                }
            }
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()

        // The spell list
        private const val NBT_SPELL_LIST = "spell_list"
        private const val NBT_SPELL = "spell"
        private const val NBT_KEYBIND = "keybind"
    }
}