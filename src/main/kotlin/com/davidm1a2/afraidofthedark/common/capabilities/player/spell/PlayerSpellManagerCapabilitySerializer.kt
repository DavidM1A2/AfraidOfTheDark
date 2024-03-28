package com.davidm1a2.afraidofthedark.common.capabilities.player.spell

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag

class PlayerSpellManagerCapabilitySerializer(instance: IPlayerSpellManager = PlayerSpellManager()) : AOTDCapabilitySerializer<IPlayerSpellManager, CompoundTag>(instance) {
    override fun getCapability() = ModCapabilities.PLAYER_SPELL_MANAGER

    override fun serializeNBT(): CompoundTag {
        val compound = CompoundTag()

        val spellListNbt = ListTag()
        for (spell in instance.getSpells()) {
            val spellListEntryNbt = CompoundTag()

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

    override fun deserializeNBTSafe(nbt: CompoundTag) {
        val spellListNbt = nbt.getList(NBT_SPELL_LIST, Tag.TAG_COMPOUND.toInt())
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
    }

    companion object {
        // The spell list
        private const val NBT_SPELL_LIST = "spell_list"
        private const val NBT_SPELL = "spell"
        private const val NBT_KEYBIND = "keybind"
    }
}