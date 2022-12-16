package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.client.particle.ArrowTrailParticle
import com.davidm1a2.afraidofthedark.client.particle.CleanseParticle
import com.davidm1a2.afraidofthedark.client.particle.DelayParticle
import com.davidm1a2.afraidofthedark.client.particle.DigParticle
import com.davidm1a2.afraidofthedark.client.particle.DisintegrateParticle
import com.davidm1a2.afraidofthedark.client.particle.DustCloudParticle
import com.davidm1a2.afraidofthedark.client.particle.EnariaFightEventParticle
import com.davidm1a2.afraidofthedark.client.particle.EnariasAltarParticle
import com.davidm1a2.afraidofthedark.client.particle.EnchantedFrogSpawnParticle
import com.davidm1a2.afraidofthedark.client.particle.EnderParticle
import com.davidm1a2.afraidofthedark.client.particle.ExplosionParticle
import com.davidm1a2.afraidofthedark.client.particle.FeedParticle
import com.davidm1a2.afraidofthedark.client.particle.FireParticle
import com.davidm1a2.afraidofthedark.client.particle.FizzleParticle
import com.davidm1a2.afraidofthedark.client.particle.FlyParticle
import com.davidm1a2.afraidofthedark.client.particle.FreezeParticle
import com.davidm1a2.afraidofthedark.client.particle.FrostPhoenixStormParticle
import com.davidm1a2.afraidofthedark.client.particle.GrowParticle
import com.davidm1a2.afraidofthedark.client.particle.HealParticle
import com.davidm1a2.afraidofthedark.client.particle.ImbueFizzleParticle
import com.davidm1a2.afraidofthedark.client.particle.ImbueParticle
import com.davidm1a2.afraidofthedark.client.particle.LightningParticle
import com.davidm1a2.afraidofthedark.client.particle.PoisonParticle
import com.davidm1a2.afraidofthedark.client.particle.ProjectileParticle
import com.davidm1a2.afraidofthedark.client.particle.RotateParticle
import com.davidm1a2.afraidofthedark.client.particle.SelfFizzleParticle
import com.davidm1a2.afraidofthedark.client.particle.SelfParticle
import com.davidm1a2.afraidofthedark.client.particle.ShieldParticle
import com.davidm1a2.afraidofthedark.client.particle.SmokeScreenParticle
import com.davidm1a2.afraidofthedark.client.particle.SonicDisruptionParticle
import com.davidm1a2.afraidofthedark.client.particle.SpellHitParticle
import com.davidm1a2.afraidofthedark.client.particle.SpellLaserParticle
import com.davidm1a2.afraidofthedark.client.particle.StrengthParticle
import com.davidm1a2.afraidofthedark.client.particle.VitaeExtractorBurnParticle
import com.davidm1a2.afraidofthedark.client.particle.VitaeExtractorChargeParticle
import com.davidm1a2.afraidofthedark.client.particle.WardParticle
import com.davidm1a2.afraidofthedark.client.particle.WeaknessParticle
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import net.minecraft.client.Minecraft
import net.minecraft.particles.ParticleType
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

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun registerParticleFactories(event: ParticleFactoryRegisterEvent) {
        val particleManager = Minecraft.getInstance().particleEngine

        particleManager.register(ModParticles.ENARIAS_ALTAR, EnariasAltarParticle::Factory)
        particleManager.register(ModParticles.ENARIA_FIGHT_EVENT, EnariaFightEventParticle::Factory)
        particleManager.register(ModParticles.ENCHANTED_FROG_SPAWN, EnchantedFrogSpawnParticle::Factory)
        particleManager.register(ModParticles.SMOKE_SCREEN, SmokeScreenParticle::Factory)
        particleManager.register(ModParticles.SPELL_HIT, SpellHitParticle::Factory)
        particleManager.register(ModParticles.SPELL_LASER, SpellLaserParticle::Factory)
        particleManager.register(ModParticles.FREEZE, FreezeParticle::Factory)
        particleManager.register(ModParticles.DIG, DigParticle::Factory)
        particleManager.register(ModParticles.ENDER, EnderParticle::Factory)
        particleManager.register(ModParticles.EXPLOSION, ExplosionParticle::Factory)
        particleManager.register(ModParticles.FIRE, FireParticle::Factory)
        particleManager.register(ModParticles.FLY, FlyParticle::Factory)
        particleManager.register(ModParticles.GROW, GrowParticle::Factory)
        particleManager.register(ModParticles.HEAL, HealParticle::Factory)
        particleManager.register(ModParticles.POISON, PoisonParticle::Factory)
        particleManager.register(ModParticles.STRENGTH, StrengthParticle::Factory)
        particleManager.register(ModParticles.WEAKNESS, WeaknessParticle::Factory)
        particleManager.register(ModParticles.VITAE_EXTRACTOR_BURN, VitaeExtractorBurnParticle::Factory)
        particleManager.register(ModParticles.VITAE_EXTRACTOR_CHARGE, VitaeExtractorChargeParticle::Factory)
        particleManager.register(ModParticles.WARD, WardParticle::Factory)
        particleManager.register(ModParticles.CLEANSE, CleanseParticle::Factory)
        particleManager.register(ModParticles.DISINTEGRATE, DisintegrateParticle::Factory)
        particleManager.register(ModParticles.FROST_PHOENIX_STORM, FrostPhoenixStormParticle::Factory)
        particleManager.register(ModParticles.FIZZLE, FizzleParticle::Factory)
        particleManager.register(ModParticles.FEED, FeedParticle::Factory)
        particleManager.register(ModParticles.LIGHTNING, LightningParticle::Factory)
        particleManager.register(ModParticles.SONIC_DISRUPTION, SonicDisruptionParticle::Factory)
        particleManager.register(ModParticles.DUST_CLOUD, DustCloudParticle::Factory)
        particleManager.register(ModParticles.ARROW_TRAIL, ArrowTrailParticle::Factory)
        particleManager.register(ModParticles.SHIELD, ShieldParticle::Factory)
        particleManager.register(ModParticles.DELAY, DelayParticle::Factory)
        particleManager.register(ModParticles.IMBUE_FIZZLE, ImbueFizzleParticle::Factory)
        particleManager.register(ModParticles.IMBUE, ImbueParticle::Factory)
        particleManager.register(ModParticles.ROTATE, RotateParticle::Factory)
        particleManager.register(ModParticles.SELF, SelfParticle::Factory)
        particleManager.register(ModParticles.SELF_FIZZLE, SelfFizzleParticle::Factory)
        particleManager.register(ModParticles.FEY, ProjectileParticle::Factory)
    }
}