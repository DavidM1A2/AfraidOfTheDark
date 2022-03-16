package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellThermalData
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

class ThermalDataPacketProcessor : PacketProcessor<ThermalDataPacket> {
    override fun encode(msg: ThermalDataPacket, buf: PacketBuffer) {
        buf.writeDouble(msg.vitae)
    }

    override fun decode(buf: PacketBuffer): ThermalDataPacket {
        return ThermalDataPacket(buf.readDouble())
    }

    override fun process(msg: ThermalDataPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player = Minecraft.getInstance().player!!
            player.getSpellThermalData().vitae = msg.vitae
        }
    }
}