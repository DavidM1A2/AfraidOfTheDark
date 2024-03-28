package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.fmllegacy.network.NetworkDirection
import net.minecraftforge.fmllegacy.network.NetworkEvent

/**
 * This is a packet that is sent to server or client to clear spells saved in the spell manager
 */
class ClearSpellsPacketProcessor : PacketProcessor<ClearSpellsPacket> {
    override fun encode(msg: ClearSpellsPacket, buf: FriendlyByteBuf) {
    }

    override fun decode(buf: FriendlyByteBuf): ClearSpellsPacket {
        return ClearSpellsPacket()
    }

    override fun process(msg: ClearSpellsPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            Minecraft.getInstance().player!!.getSpellManager().clearSpells()
        } else if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            ctx.sender!!.getSpellManager().clearSpells()
        }
    }
}