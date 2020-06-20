package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets

import net.minecraft.util.math.Vec3d

class FreezeDataPacket(
    internal val freezeTicks: Int,
    internal val position: Vec3d?,
    internal val yaw: Float,
    internal val pitch: Float
)