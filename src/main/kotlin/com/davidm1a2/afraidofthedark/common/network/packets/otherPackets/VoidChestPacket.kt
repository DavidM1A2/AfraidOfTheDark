package com.davidm1a2.afraidofthedark.common.network.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.network.packets.EntityPacket
import net.minecraft.entity.Entity
import java.util.*

class VoidChestPacket(
    entityUUID: UUID,
    entityID: Int,
    internal val chestX: Int,
    internal val chestY: Int,
    internal val chestZ: Int
) : EntityPacket(entityUUID, entityID) {
    constructor(entity: Entity, chestX: Int, chestY: Int, chestZ: Int) : this(
        entity.uniqueID,
        entity.entityId,
        chestX,
        chestY,
        chestZ
    )
}