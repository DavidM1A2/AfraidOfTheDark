package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.network.packets.EntityPacket
import net.minecraft.entity.Entity
import java.util.UUID

class PlayEnariasFightMusicPacket(
    entityUUID: UUID,
    entityID: Int,
    internal val playMusic: Boolean
) : EntityPacket(entityUUID, entityID) {
    constructor(entity: Entity) : this(
        entity.uuid,
        entity.id,
        true
    )

    constructor() : this(
        UUID.randomUUID(),
        0,
        false
    )
}