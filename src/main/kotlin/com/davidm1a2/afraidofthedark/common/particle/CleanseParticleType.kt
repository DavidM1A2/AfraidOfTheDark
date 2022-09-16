package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.particle.base.AOTDParticleType
import com.mojang.serialization.Codec

class CleanseParticleType : AOTDParticleType<CleanseParticleData>("cleanse", false, CleanseParticleData.DESERIALIZER) {
    override fun codec(): Codec<CleanseParticleData> {
        return CleanseParticleData.CODEC
    }
}