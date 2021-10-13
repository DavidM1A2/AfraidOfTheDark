package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import com.davidm1a2.afraidofthedark.common.registry.MeteorEntry

data class WatchedMeteor(
    val meteor: MeteorEntry,
    val accuracy: Int,
    val dropAngle: Int,
    val latitude: Int,
    val longitude: Int,
)