package com.davidm1a2.afraidofthedark.common.network.packets.capability

import net.minecraft.world.phys.Vec3

class SpellFreezeDataPacket(
    internal val freezeTicks: Int,
    internal val position: Vec3?,
    internal val yaw: Float,
    internal val pitch: Float
)