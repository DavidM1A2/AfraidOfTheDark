package com.davidm1a2.afraidofthedark.common.network.dataserializer

import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.network.PacketBuffer
import net.minecraft.network.datasync.IDataSerializer

class SpellDataSerializer : IDataSerializer<Spell> {
    override fun write(buffer: PacketBuffer, spell: Spell) {
        buffer.writeNbt(spell.serializeNBT())
    }

    override fun read(buffer: PacketBuffer): Spell {
        return Spell(buffer.readNbt()!!)
    }

    override fun copy(spell: Spell): Spell {
        return Spell(spell.serializeNBT())
    }
}