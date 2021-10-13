package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.WatchedMeteor

class UpdateWatchedMeteorPacket(
    internal val watchedMeteor: WatchedMeteor?
)