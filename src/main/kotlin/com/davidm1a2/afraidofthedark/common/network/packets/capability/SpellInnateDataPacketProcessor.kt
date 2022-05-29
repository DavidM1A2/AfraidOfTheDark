package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellInnateData
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

class SpellInnateDataPacketProcessor : PacketProcessor<SpellInnateDataPacket> {
    override fun encode(msg: SpellInnateDataPacket, buf: PacketBuffer) {
        buf.writeDouble(msg.vitae)
    }

    override fun decode(buf: PacketBuffer): SpellInnateDataPacket {
        return SpellInnateDataPacket(buf.readDouble())
    }

    override fun process(msg: SpellInnateDataPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player = Minecraft.getInstance().player!!
            player.getSpellInnateData().vitae = msg.vitae
        }
    }
}