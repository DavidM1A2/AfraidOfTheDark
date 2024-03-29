package com.davidm1a2.afraidofthedark.common.network.packets.animation

import com.davidm1a2.afraidofthedark.common.network.packets.EntityPacket
import net.minecraft.entity.Entity
import java.util.UUID

class AnimationPacket(
    entityUUID: UUID,
    entityID: Int,
    internal val animationName: String,
    internal val higherPriorityAnims: Array<String> = emptyArray()
) : EntityPacket(entityUUID, entityID) {
    constructor(entity: Entity, animationName: String, vararg higherPriorityAnims: String) : this(
        entity.uuid,
        entity.id,
        animationName,
        arrayOf(*higherPriorityAnims)
    )
}