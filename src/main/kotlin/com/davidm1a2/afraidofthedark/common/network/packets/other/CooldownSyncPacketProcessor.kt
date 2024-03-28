package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.item.core.AOTDPerItemCooldownItem
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraftforge.fmllegacy.network.NetworkDirection
import net.minecraftforge.fmllegacy.network.NetworkEvent

/**
 * Packet used to update the client of the server-client time difference used to show item cooldowns
 */
class CooldownSyncPacketProcessor : PacketProcessor<CooldownSyncPacket> {
    override fun encode(msg: CooldownSyncPacket, buf: FriendlyByteBuf) {
        buf.writeLong(msg.timeServer)
        buf.writeInt(Item.getId(msg.itemToSync))
    }

    override fun decode(buf: FriendlyByteBuf): CooldownSyncPacket {
        return CooldownSyncPacket(
            buf.readLong(),
            Item.byId(buf.readInt()) as AOTDPerItemCooldownItem
        )
    }

    override fun process(msg: CooldownSyncPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            // Compute the difference between client and server time and store that info
            msg.itemToSync.updateServerClientDifference(System.currentTimeMillis() - msg.timeServer)
        }
    }
}