package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.event.custom.PlayerStartedAfraidOfTheDarkEvent
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fmllegacy.network.NetworkDirection
import net.minecraftforge.fmllegacy.network.NetworkEvent

/**
 * This is a packet that is sent from a client to the server that tells the server a player has begun the mod
 */
class StartAOTDPacketProcessor : PacketProcessor<StartAOTDPacket> {
    override fun encode(msg: StartAOTDPacket, buf: FriendlyByteBuf) {
    }

    override fun decode(buf: FriendlyByteBuf): StartAOTDPacket {
        return StartAOTDPacket()
    }

    override fun process(msg: StartAOTDPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            MinecraftForge.EVENT_BUS.post(PlayerStartedAfraidOfTheDarkEvent(ctx.sender!!))
        }
    }
}