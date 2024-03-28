package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellThermalData
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.fmllegacy.network.NetworkDirection
import net.minecraftforge.fmllegacy.network.NetworkEvent

class SpellThermalDataPacketProcessor : PacketProcessor<SpellThermalDataPacket> {
    override fun encode(msg: SpellThermalDataPacket, buf: FriendlyByteBuf) {
        buf.writeDouble(msg.vitae)
    }

    override fun decode(buf: FriendlyByteBuf): SpellThermalDataPacket {
        return SpellThermalDataPacket(buf.readDouble())
    }

    override fun process(msg: SpellThermalDataPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player = Minecraft.getInstance().player!!
            player.getSpellThermalData().vitae = msg.vitae
        }
    }
}