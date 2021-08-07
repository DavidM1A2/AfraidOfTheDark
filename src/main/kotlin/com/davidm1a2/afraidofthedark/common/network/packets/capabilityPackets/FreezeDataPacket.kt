package com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets

import net.minecraft.util.math.vector.Vector3d

class FreezeDataPacket(
    internal val freezeTicks: Int,
    internal val position: Vector3d?,
    internal val yaw: Float,
    internal val pitch: Float
)