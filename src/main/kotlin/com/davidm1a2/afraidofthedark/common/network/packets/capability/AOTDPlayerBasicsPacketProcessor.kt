package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * This is a packet that is sent to a client or from a client to the server that updates the status of all player basics
 */
class AOTDPlayerBasicsPacketProcessor : PacketProcessor<AOTDPlayerBasicsPacket> {
    override fun encode(msg: AOTDPlayerBasicsPacket, buf: PacketBuffer) {
        buf.writeNbt(msg.data)
    }

    override fun decode(buf: PacketBuffer): AOTDPlayerBasicsPacket {
        return AOTDPlayerBasicsPacket(buf.readNbt()!!)
    }

    override fun process(msg: AOTDPlayerBasicsPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            // Grab the current player's capabilities
            val playerBasics = Minecraft.getInstance().player!!.getBasics()

            // Read the new capabilities into the player's data
            ModCapabilities.PLAYER_BASICS.storage.readNBT(ModCapabilities.PLAYER_BASICS, playerBasics, null, msg.data)
        } else if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            // Send the player his/her current capabilities in a packet as requested
            ctx.sender!!.getBasics().syncAll(ctx.sender!!)
        }
    }
}