package com.davidm1a2.afraidofthedark.common.particle.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.core.particles.SimpleParticleType

class BasicAOTDParticleType(name: String, alwaysShow: Boolean = false) : SimpleParticleType(alwaysShow) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }
}