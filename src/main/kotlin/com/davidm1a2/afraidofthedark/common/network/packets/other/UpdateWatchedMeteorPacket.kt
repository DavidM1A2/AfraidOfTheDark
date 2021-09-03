package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.registry.MeteorEntry

class UpdateWatchedMeteorPacket(
    internal val meteorEntry: MeteorEntry?,
    internal val accuracy: Int,
    internal val dropAngle: Int = 0,
    internal val latitude: Int = 0,
    internal val longitude: Int = 0
)