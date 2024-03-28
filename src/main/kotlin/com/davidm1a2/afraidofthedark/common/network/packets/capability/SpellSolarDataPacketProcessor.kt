package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellSolarData
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.fmllegacy.network.NetworkDirection
import net.minecraftforge.fmllegacy.network.NetworkEvent

class SpellSolarDataPacketProcessor : PacketProcessor<SpellSolarDataPacket> {
    override fun encode(msg: SpellSolarDataPacket, buf: FriendlyByteBuf) {
        buf.writeDouble(msg.vitae)
    }

    override fun decode(buf: FriendlyByteBuf): SpellSolarDataPacket {
        return SpellSolarDataPacket(buf.readDouble())
    }

    override fun process(msg: SpellSolarDataPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player = Minecraft.getInstance().player!!
            player.getSpellSolarData().vitae = msg.vitae
        }
    }
}