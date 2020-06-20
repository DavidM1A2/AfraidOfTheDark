package com.davidm1a2.afraidofthedark.common.packets

import com.davidm1a2.afraidofthedark.common.packets.packetHandler.PacketProcessor
import net.minecraft.network.PacketBuffer
import java.util.*

/**
 * Class used to encapsulate sending data about non-player entities
 */
abstract class EntityPacketProcessor<T : EntityPacket> : PacketProcessor<T> {
    fun writeEntityData(msg: T, packetBuffer: PacketBuffer) {
        packetBuffer.writeLong(msg.entityUUID.mostSignificantBits)
        packetBuffer.writeLong(msg.entityUUID.leastSignificantBits)
        packetBuffer.writeInt(msg.entityID)
    }

    fun readEntityData(buf: PacketBuffer): Pair<UUID, Int> {
        return UUID(buf.readLong(), buf.readLong()) to buf.readInt()
    }
}