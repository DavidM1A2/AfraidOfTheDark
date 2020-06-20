package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.packets.EntityPacket
import net.minecraft.entity.Entity
import java.util.*

class PlayEnariasFightMusicPacket(
    entityUUID: UUID,
    entityID: Int,
    internal val playMusic: Boolean
) : EntityPacket(entityUUID, entityID) {
    constructor(entity: Entity, playMusic: Boolean) : this(
        entity.uniqueID,
        entity.entityId,
        playMusic
    )
}