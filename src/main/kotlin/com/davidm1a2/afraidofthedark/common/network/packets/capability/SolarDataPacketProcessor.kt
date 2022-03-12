package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellSolarData
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

class SolarDataPacketProcessor : PacketProcessor<SolarDataPacket> {
    override fun encode(msg: SolarDataPacket, buf: PacketBuffer) {
        buf.writeDouble(msg.vitae)
    }

    override fun decode(buf: PacketBuffer): SolarDataPacket {
        return SolarDataPacket(buf.readDouble())
    }

    override fun process(msg: SolarDataPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player = Minecraft.getInstance().player!!
            player.getSpellSolarData().vitae = msg.vitae
        }
    }
}