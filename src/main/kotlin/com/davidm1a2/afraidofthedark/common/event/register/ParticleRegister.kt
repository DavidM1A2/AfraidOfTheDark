package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import net.minecraft.util.registry.IRegistry

object ParticleRegister {
    fun register() {
        for (particle in ModParticles.PARTICLE_LIST) {
            IRegistry.field_212632_u.put(particle.id, particle)
        }
    }
}