package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellLunarData
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

class SpellLunarDataPacketProcessor : PacketProcessor<SpellLunarDataPacket> {
    override fun encode(msg: SpellLunarDataPacket, buf: PacketBuffer) {
        buf.writeDouble(msg.vitae)
    }

    override fun decode(buf: PacketBuffer): SpellLunarDataPacket {
        return SpellLunarDataPacket(buf.readDouble())
    }

    override fun process(msg: SpellLunarDataPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player = Minecraft.getInstance().player!!
            player.getSpellLunarData().vitae = msg.vitae
        }
    }
}