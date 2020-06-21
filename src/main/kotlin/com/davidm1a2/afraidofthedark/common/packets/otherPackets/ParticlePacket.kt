package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry
import net.minecraft.util.math.Vec3d

class ParticlePacket(
    internal val particle: AOTDParticleRegistry.ParticleTypes,
    internal val positions: List<Vec3d>,
    internal val speeds: List<Vec3d>
)