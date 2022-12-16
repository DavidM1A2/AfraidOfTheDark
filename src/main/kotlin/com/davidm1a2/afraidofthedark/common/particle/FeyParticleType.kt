package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.particle.base.AOTDParticleType
import com.mojang.serialization.Codec

class FeyParticleType : AOTDParticleType<FeyParticleData>("fey", false, FeyParticleData.DESERIALIZER) {
    override fun codec(): Codec<FeyParticleData> {
        return FeyParticleData.CODEC
    }
}