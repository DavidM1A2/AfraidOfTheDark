package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.client.particle.*
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import net.minecraft.client.Minecraft
import net.minecraft.particles.ParticleType
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class ParticleRegister {
    /**
     * Called by forge to register any of our particles
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerParticleTypes(event: RegistryEvent.Register<ParticleType<*>>) {
        val registry = event.registry

        // Register each item in our item list
        registry.registerAll(*ModParticles.PARTICLE_LIST)
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun registerParticleFactories(event: ParticleFactoryRegisterEvent) {
        val particleManager = Minecraft.getInstance().particles

        particleManager.registerFactory(ModParticles.ENARIA_BASIC_ATTACK, EnariaBasicAttackParticle::Factory)
        particleManager.registerFactory(ModParticles.ENARIAS_ALTAR, EnariasAltarParticle::Factory)
        particleManager.registerFactory(ModParticles.ENARIA_SPELL_CAST, EnariaSpellCastParticle::Factory)
        particleManager.registerFactory(ModParticles.ENARIA_SPELL_CAST_2, EnariaSpellCast2Particle::Factory)
        particleManager.registerFactory(ModParticles.ENARIA_TELEPORT, EnariaTeleportParticle::Factory)
        particleManager.registerFactory(ModParticles.ENCHANTED_FROG_SPAWN, EnchantedFrogSpawnParticle::Factory)
        particleManager.registerFactory(ModParticles.SMOKE_SCREEN, SmokeScreenParticle::Factory)
        particleManager.registerFactory(ModParticles.SPELL_CAST, SpellCastParticle::Factory)
        particleManager.registerFactory(ModParticles.SPELL_HIT, SpellHitParticle::Factory)
        particleManager.registerFactory(ModParticles.SPELL_LASER, SpellLaserParticle::Factory)
        particleManager.registerFactory(ModParticles.FREEZE, FreezeParticle::Factory)
    }
}