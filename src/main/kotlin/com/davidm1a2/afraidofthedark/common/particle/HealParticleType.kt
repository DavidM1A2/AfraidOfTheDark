package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.particle.base.AOTDParticleType
import com.mojang.serialization.Codec

class HealParticleType : AOTDParticleType<HealParticleData>("heal", false, HealParticleData.DESERIALIZER) {
    override fun codec(): Codec<HealParticleData> {
        return HealParticleData.CODEC
    }
}