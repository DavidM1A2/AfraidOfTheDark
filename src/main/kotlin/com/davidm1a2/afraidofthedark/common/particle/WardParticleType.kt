package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.particle.base.AOTDParticleType
import com.mojang.serialization.Codec

class WardParticleType : AOTDParticleType<WardParticleData>("ward", false, WardParticleData.DESERIALIZER) {
    override fun codec(): Codec<WardParticleData> {
        return WardParticleData.CODEC
    }
}