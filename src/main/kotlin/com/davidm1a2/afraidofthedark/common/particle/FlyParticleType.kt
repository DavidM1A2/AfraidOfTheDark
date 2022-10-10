package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.particle.base.AOTDParticleType
import com.mojang.serialization.Codec

class FlyParticleType : AOTDParticleType<FlyParticleData>("fly", false, FlyParticleData.DESERIALIZER) {
    override fun codec(): Codec<FlyParticleData> {
        return FlyParticleData.CODEC
    }
}