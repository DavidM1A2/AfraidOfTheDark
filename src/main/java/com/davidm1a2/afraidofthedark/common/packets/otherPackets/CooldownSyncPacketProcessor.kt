package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemWithPerItemCooldown
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.PacketProcessor
import net.minecraft.item.Item
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Packet used to update the client of the server-client time difference used to show item cooldowns
 */
class CooldownSyncPacketProcessor : PacketProcessor<CooldownSyncPacket> {
    override fun encode(msg: CooldownSyncPacket, buf: PacketBuffer) {
        buf.writeLong(msg.timeServer)
        buf.writeInt(Item.getIdFromItem(msg.itemToSync))
    }

    override fun decode(buf: PacketBuffer): CooldownSyncPacket {
        return CooldownSyncPacket(
            buf.readLong(),
            Item.getItemById(buf.readInt()) as AOTDItemWithPerItemCooldown
        )
    }

    override fun process(msg: CooldownSyncPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            // Compute the difference between client and server time and store that info
            msg.itemToSync.updateServerClientDifference(System.currentTimeMillis() - msg.timeServer)
        }
    }
}