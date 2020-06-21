package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * This is a packet that is sent to server or client to clear spells saved in the spell manager
 */
class ClearSpellsPacketProcessor : PacketProcessor<ClearSpellsPacket> {
    override fun encode(msg: ClearSpellsPacket, buf: PacketBuffer) {
    }

    override fun decode(buf: PacketBuffer): ClearSpellsPacket {
        return ClearSpellsPacket()
    }

    override fun process(msg: ClearSpellsPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            Minecraft.getInstance().player.getSpellManager().clearSpells()
        } else if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            ctx.sender!!.getSpellManager().clearSpells()
        }
    }
}