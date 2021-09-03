package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * This is a packet that is sent to server or client to ensure a given spell is saved in the spell manager
 */
class SpellPacketProcessor : PacketProcessor<SpellPacket> {
    override fun encode(msg: SpellPacket, buf: PacketBuffer) {
        // First write if we have a keybind
        val hasKeybind = msg.keybind != null
        buf.writeBoolean(hasKeybind)

        // Then write the keybind out
        if (hasKeybind) {
            buf.writeUtf(msg.keybind!!)
        }

        // Finally write the spell NBT
        buf.writeNbt(msg.spell.serializeNBT())
    }

    override fun decode(buf: PacketBuffer): SpellPacket {
        // First test if we have a keybind
        val hasKeybind = buf.readBoolean()
        val keybind = if (hasKeybind) buf.readUtf() else null

        return SpellPacket(Spell(buf.readNbt()!!), keybind)
    }

    override fun process(msg: SpellPacket, ctx: NetworkEvent.Context) {
        val spellManager = when (ctx.direction) {
            NetworkDirection.PLAY_TO_CLIENT -> Minecraft.getInstance().player!!.getSpellManager()
            NetworkDirection.PLAY_TO_SERVER -> ctx.sender!!.getSpellManager()
            else -> null
        }

        spellManager?.let {
            // Add the spell and keybind it if necessary
            spellManager.addOrUpdateSpell(msg.spell)
            if (msg.keybind != null) {
                spellManager.keybindSpell(msg.keybind, msg.spell)
            }
        }
    }
}