package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.particle.base.AOTDParticleType
import com.mojang.serialization.Codec

class ShieldParticleType : AOTDParticleType<ShieldParticleData>("shield", false, ShieldParticleData.DESERIALIZER) {
    override fun codec(): Codec<ShieldParticleData> {
        return ShieldParticleData.CODEC
    }
}