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
    fun registerParticleFactories(event: ParticleFactoryRegisterEvent) {
        val particleManager = Minecraft.getInstance().particles

        particleManager.registerFactory(ModParticles.ENARIA_BASIC_ATTACK) { _, worldIn, x, y, z, _, _, _ -> EnariaBasicAttackParticle(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.ENARIAS_ALTAR) { _, worldIn, x, y, z, _, _, _ -> EnariasAltarParticle(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.ENARIA_SPELL_CAST) { _, worldIn, x, y, z, _, _, _ -> EnariaSpellCastParticle(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.ENARIA_SPELL_CAST_2) { _, worldIn, x, y, z, xSpeed, _, zSpeed ->
            EnariaSpellCast2Particle(
                worldIn,
                x,
                y,
                z,
                xSpeed,
                zSpeed
            )
        }
        particleManager.registerFactory(ModParticles.ENARIA_TELEPORT) { _, worldIn, x, y, z, _, _, _ -> EnariaTeleportParticle(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.ENCHANTED_FROG_SPAWN) { _, worldIn, x, y, z, xSpeed, _, zSpeed ->
            EnchantedFrogSpawnParticle(
                worldIn,
                x,
                y,
                z,
                xSpeed,
                zSpeed
            )
        }
        particleManager.registerFactory(ModParticles.SMOKE_SCREEN) { _, worldIn, x, y, z, _, _, _ -> SmokeScreenParticle(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.SPELL_CAST) { _, worldIn, x, y, z, _, _, _ -> SpellCastParticle(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.SPELL_HIT) { _, worldIn, x, y, z, _, _, _ -> SpellHitParticle(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.SPELL_LASER) { _, worldIn, x, y, z, _, _, _ -> SpellLaserParticle(worldIn, x, y, z) }
    }
}