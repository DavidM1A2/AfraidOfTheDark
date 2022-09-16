package com.davidm1a2.afraidofthedark.common.particle.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.particles.BasicParticleType

class BasicAOTDParticleType(name: String, alwaysShow: Boolean = false) : BasicParticleType(alwaysShow) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }
}