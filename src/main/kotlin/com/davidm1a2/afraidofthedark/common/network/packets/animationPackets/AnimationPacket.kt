package com.davidm1a2.afraidofthedark.common.network.packets.animationPackets

import com.davidm1a2.afraidofthedark.common.network.packets.EntityPacket
import net.minecraft.entity.Entity
import java.util.*

class AnimationPacket(
    entityUUID: UUID,
    entityID: Int,
    internal val animationName: String,
    internal val higherPriorityAnims: Array<String> = emptyArray()
) : EntityPacket(entityUUID, entityID) {
    constructor(entity: Entity, animationName: String, vararg higherPriorityAnims: String) : this(
        entity.uniqueID,
        entity.entityId,
        animationName,
        arrayOf(*higherPriorityAnims)
    )
}