package com.davidm1a2.afraidofthedark.common.network.packets.otherPackets

import net.minecraft.particles.IParticleData
import net.minecraft.util.math.Vec3d

class ParticlePacket(
    internal val particle: IParticleData,
    internal val positions: List<Vec3d>,
    internal val speeds: List<Vec3d>
)