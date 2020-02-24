package com.davidm1a2.afraidofthedark.common.packets

import io.netty.buffer.ByteBuf
import net.minecraft.entity.Entity
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import java.util.*

/**
 * Class used to encapsulate sending data about non-player entities
 *
 * @property entityUUID UUID for client to server
 * @property entityID Int ID for server to client
 */
abstract class EntitySyncBase : IMessage {
    protected lateinit var entityUUID: UUID
    protected var entityID: Int

    /**
     * Required default constructor for all packets
     */
    constructor() {
        entityID = 0
    }

    /**
     * Primary constructor used to initializes this packet with the entity to sync
     *
     * @param entity The entity to sync
     */
    constructor(entity: Entity) {
        entityUUID = entity.persistentID
        entityID = entity.entityId
    }

    /**
     * Deserializes the UUID and ID
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf) {
        entityUUID = UUID(buf.readLong(), buf.readLong())
        entityID = buf.readInt()
    }

    /**
     * Serializes the UUID and ID
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf) {
        buf.writeLong(entityUUID.mostSignificantBits)
        buf.writeLong(entityUUID.leastSignificantBits)
        buf.writeInt(entityID)
    }
}