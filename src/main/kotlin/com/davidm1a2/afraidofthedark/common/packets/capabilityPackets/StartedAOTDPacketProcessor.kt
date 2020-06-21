package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * This is a packet that is sent to a client or from a client to the server that updates the status of if the player has begun the mod
 */
class StartedAOTDPacketProcessor : PacketProcessor<StartedAOTDPacket> {
    override fun encode(msg: StartedAOTDPacket, buf: PacketBuffer) {
        buf.writeBoolean(msg.startedAOTD)
    }

    override fun decode(buf: PacketBuffer): StartedAOTDPacket {
        return StartedAOTDPacket(buf.readBoolean())
    }

    override fun process(msg: StartedAOTDPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            Minecraft.getInstance().player.getBasics().startedAOTD = msg.startedAOTD
        } else if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            ctx.sender!!.getBasics().startedAOTD = msg.startedAOTD
        }
    }
}