package com.davidm1a2.afraidofthedark.common.network.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.network.packets.EntityPacket
import net.minecraft.entity.Entity
import java.util.*

class PlayEnariasFightMusicPacket(
    entityUUID: UUID,
    entityID: Int,
    internal val playMusic: Boolean
) : EntityPacket(entityUUID, entityID) {
    constructor(entity: Entity) : this(
        entity.uniqueID,
        entity.entityId,
        true
    )

    constructor() : this(
        UUID.randomUUID(),
        0,
        false
    )
}