package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.particle.base.AOTDParticleType
import com.mojang.serialization.Codec

class SelfParticleType : AOTDParticleType<SelfParticleData>("self", false, SelfParticleData.DESERIALIZER) {
    override fun codec(): Codec<SelfParticleData> {
        return SelfParticleData.CODEC
    }
}