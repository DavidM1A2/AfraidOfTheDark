package com.davidm1a2.afraidofthedark.common.network.dataserializer

import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.syncher.EntityDataSerializer

class SpellDataSerializer : EntityDataSerializer<Spell> {
    override fun write(buffer: FriendlyByteBuf, spell: Spell) {
        buffer.writeNbt(spell.serializeNBT())
    }

    override fun read(buffer: FriendlyByteBuf): Spell {
        return Spell(buffer.readNbt()!!)
    }

    override fun copy(spell: Spell): Spell {
        return Spell(spell.serializeNBT())
    }
}