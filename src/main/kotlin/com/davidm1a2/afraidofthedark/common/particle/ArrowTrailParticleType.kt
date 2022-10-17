package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.particle.base.AOTDParticleType
import com.mojang.serialization.Codec

class ArrowTrailParticleType : AOTDParticleType<ArrowTrailParticleData>("arrow_trail", false, ArrowTrailParticleData.DESERIALIZER) {
    override fun codec(): Codec<ArrowTrailParticleData> {
        return ArrowTrailParticleData.CODEC
    }
}