package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellInnateData
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

class InnateDataPacketProcessor : PacketProcessor<InnateDataPacket> {
    override fun encode(msg: InnateDataPacket, buf: PacketBuffer) {
        buf.writeDouble(msg.vitae)
    }

    override fun decode(buf: PacketBuffer): InnateDataPacket {
        return InnateDataPacket(buf.readDouble())
    }

    override fun process(msg: InnateDataPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player = Minecraft.getInstance().player!!
            player.getSpellInnateData().vitae = msg.vitae
        }
    }
}