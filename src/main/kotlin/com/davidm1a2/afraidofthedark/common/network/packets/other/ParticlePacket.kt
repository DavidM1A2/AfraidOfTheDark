package com.davidm1a2.afraidofthedark.common.network.packets.other

import net.minecraft.particles.IParticleData
import net.minecraft.util.math.vector.Vector3d

class ParticlePacket(
    internal val particle: IParticleData,
    internal val positions: List<Vector3d>,
    internal val speeds: List<Vector3d>
)