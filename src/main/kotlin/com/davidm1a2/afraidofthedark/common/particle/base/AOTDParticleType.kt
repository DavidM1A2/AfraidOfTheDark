package com.davidm1a2.afraidofthedark.common.particle.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType

abstract class AOTDParticleType<T : ParticleOptions>(name: String, alwaysShow: Boolean, deserializer: ParticleOptions.Deserializer<T>) : ParticleType<T>(alwaysShow, deserializer) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }
}