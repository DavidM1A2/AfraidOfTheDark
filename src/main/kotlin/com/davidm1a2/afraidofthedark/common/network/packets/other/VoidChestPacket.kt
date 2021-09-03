package com.davidm1a2.afraidofthedark.common.network.packets.other

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
        entity.uuid,
        entity.id,
        chestX,
        chestY,
        chestZ
    )
}