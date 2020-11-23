package com.davidm1a2.afraidofthedark.common.network.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.network.packets.packetHandler.PacketProcessor
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Packet sent from client to server to tell the server that a spell keybinding was pressed
 */
class SpellKeyPressPacketProcessor : PacketProcessor<SpellKeyPressPacket> {
    override fun encode(msg: SpellKeyPressPacket, buf: PacketBuffer) {
        buf.writeString(msg.keyPressedName)
    }

    override fun decode(buf: PacketBuffer): SpellKeyPressPacket {
        return SpellKeyPressPacket(buf.readString(500))
    }

    override fun process(msg: SpellKeyPressPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            val player = ctx.sender!!
            // Grab the player's spell manager
            val spellManager = player.getSpellManager()

            // Ensure the keybinding exists
            if (spellManager.keybindExists(msg.keyPressedName)) {
                // Grab the spell to fire, and cast it
                val spellToFire = spellManager.getSpellForKeybinding(msg.keyPressedName)
                spellToFire!!.attemptToCast(player)
            }
        }
    }
}