package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import net.minecraft.particles.ParticleType
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class ParticleRegister {
    /**
     * Called by forge to register any of our particles
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<ParticleType<*>>) {
        val registry = event.registry

        // Register each item in our item list
        registry.registerAll(*ModParticles.PARTICLE_LIST)
    }
}