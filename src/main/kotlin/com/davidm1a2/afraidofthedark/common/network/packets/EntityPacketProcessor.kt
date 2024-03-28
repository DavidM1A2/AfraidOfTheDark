package com.davidm1a2.afraidofthedark.common.network.packets

import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.network.FriendlyByteBuf
import java.util.*

/**
 * Class used to encapsulate sending data about non-player entities
 */
abstract class EntityPacketProcessor<T : EntityPacket> : PacketProcessor<T> {
    fun writeEntityData(msg: T, packetBuffer: FriendlyByteBuf) {
        packetBuffer.writeLong(msg.entityUUID.mostSignificantBits)
        packetBuffer.writeLong(msg.entityUUID.leastSignificantBits)
        packetBuffer.writeInt(msg.entityID)
    }

    fun readEntityData(buf: FriendlyByteBuf): Pair<UUID, Int> {
        return UUID(buf.readLong(), buf.readLong()) to buf.readInt()
    }
}