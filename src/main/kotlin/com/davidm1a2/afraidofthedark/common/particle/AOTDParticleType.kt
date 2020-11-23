package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.particles.BasicParticleType

abstract class AOTDParticleType(name: String, alwaysShow: Boolean) : BasicParticleType(alwaysShow) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }
}