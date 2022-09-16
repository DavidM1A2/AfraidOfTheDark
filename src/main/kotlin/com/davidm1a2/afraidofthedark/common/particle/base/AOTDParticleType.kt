package com.davidm1a2.afraidofthedark.common.particle.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType

abstract class AOTDParticleType<T : IParticleData>(name: String, alwaysShow: Boolean, deserializer: IParticleData.IDeserializer<T>) : ParticleType<T>(alwaysShow, deserializer) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }
}