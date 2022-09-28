package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.particle.base.AOTDParticleType
import com.mojang.serialization.Codec

class FeedParticleType : AOTDParticleType<FeedParticleData>("feed", false, FeedParticleData.DESERIALIZER) {
    override fun codec(): Codec<FeedParticleData> {
        return FeedParticleData.CODEC
    }
}