package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.particle.base.AOTDParticleType
import com.mojang.serialization.Codec

class ProjectileParticleType : AOTDParticleType<ProjectileParticleData>("projectile", false, ProjectileParticleData.DESERIALIZER) {
    override fun codec(): Codec<ProjectileParticleData> {
        return ProjectileParticleData.CODEC
    }
}