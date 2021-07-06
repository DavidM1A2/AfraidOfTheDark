package com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.network.packets.packetHandler.PacketProcessor
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Packet for the client to tell the server what bolt type they currently have selected
 */
class SelectedWristCrossbowBoltPacketProcessor : PacketProcessor<SelectedWristCrossbowBoltPacket> {
    override fun encode(msg: SelectedWristCrossbowBoltPacket, buf: PacketBuffer) {
        buf.writeInt(msg.selectedWristCrossbowBoltIndex)
    }

    override fun decode(buf: PacketBuffer): SelectedWristCrossbowBoltPacket {
        return SelectedWristCrossbowBoltPacket(buf.readInt())
    }

    override fun process(msg: SelectedWristCrossbowBoltPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            ctx.sender!!.getBasics().selectedWristCrossbowBoltIndex = msg.selectedWristCrossbowBoltIndex
        }
    }
}