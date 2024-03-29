package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.particle.ArrowTrailParticleType
import com.davidm1a2.afraidofthedark.common.particle.CleanseParticleType
import com.davidm1a2.afraidofthedark.common.particle.FeedParticleType
import com.davidm1a2.afraidofthedark.common.particle.FeyParticleType
import com.davidm1a2.afraidofthedark.common.particle.FlyParticleType
import com.davidm1a2.afraidofthedark.common.particle.HealParticleType
import com.davidm1a2.afraidofthedark.common.particle.ProjectileParticleType
import com.davidm1a2.afraidofthedark.common.particle.SelfParticleType
import com.davidm1a2.afraidofthedark.common.particle.ShieldParticleType
import com.davidm1a2.afraidofthedark.common.particle.WardParticleType
import com.davidm1a2.afraidofthedark.common.particle.base.BasicAOTDParticleType

object ModParticles {
    val ENARIAS_ALTAR = BasicAOTDParticleType("enarias_altar")
    val ENARIA_FIGHT_EVENT = BasicAOTDParticleType("enaria_fight_event")
    val ENCHANTED_FROG_SPAWN = BasicAOTDParticleType("enchanted_frog_spawn")
    val SMOKE_SCREEN = BasicAOTDParticleType("smoke_screen", true)
    val SPELL_HIT = BasicAOTDParticleType("spell_hit")
    val SPELL_LASER = BasicAOTDParticleType("spell_laser")
    val FREEZE = BasicAOTDParticleType("freeze")
    val DIG = BasicAOTDParticleType("dig")
    val ENDER = BasicAOTDParticleType("ender")
    val EXPLOSION = BasicAOTDParticleType("explosion")
    val FIRE = BasicAOTDParticleType("fire")
    val FLY = FlyParticleType()
    val GROW = BasicAOTDParticleType("grow")
    val HEAL = HealParticleType()
    val POISON = BasicAOTDParticleType("poison")
    val STRENGTH = BasicAOTDParticleType("strength")
    val WEAKNESS = BasicAOTDParticleType("weakness")
    val VITAE_EXTRACTOR_BURN = BasicAOTDParticleType("vitae_extractor_burn")
    val VITAE_EXTRACTOR_CHARGE = BasicAOTDParticleType("vitae_extractor_charge")
    val WARD = WardParticleType()
    val CLEANSE = CleanseParticleType()
    val DISINTEGRATE = BasicAOTDParticleType("disintegrate")
    val FROST_PHOENIX_STORM = BasicAOTDParticleType("frost_phoenix_storm")
    val FIZZLE = BasicAOTDParticleType("fizzle")
    val FEED = FeedParticleType()
    val LIGHTNING = BasicAOTDParticleType("lightning")
    val SONIC_DISRUPTION = BasicAOTDParticleType("sonic_disruption")
    val DUST_CLOUD = BasicAOTDParticleType("dust_cloud")
    val ARROW_TRAIL = ArrowTrailParticleType()
    val SHIELD = ShieldParticleType()
    val DELAY = BasicAOTDParticleType("delay")
    val IMBUE_FIZZLE = BasicAOTDParticleType("imbue_fizzle")
    val IMBUE = BasicAOTDParticleType("imbue")
    val ROTATE = BasicAOTDParticleType("rotate")
    val SELF = SelfParticleType()
    val SELF_FIZZLE = BasicAOTDParticleType("self_fizzle")
    val PROJECTILE = ProjectileParticleType()
    val FEY = FeyParticleType()

    val PARTICLE_LIST = arrayOf(
        ENARIAS_ALTAR,
        ENARIA_FIGHT_EVENT,
        ENCHANTED_FROG_SPAWN,
        SMOKE_SCREEN,
        SPELL_HIT,
        SPELL_LASER,
        FREEZE,
        DIG,
        ENDER,
        EXPLOSION,
        FIRE,
        FLY,
        GROW,
        HEAL,
        POISON,
        STRENGTH,
        WEAKNESS,
        VITAE_EXTRACTOR_BURN,
        VITAE_EXTRACTOR_CHARGE,
        WARD,
        CLEANSE,
        DISINTEGRATE,
        FROST_PHOENIX_STORM,
        FIZZLE,
        FEED,
        LIGHTNING,
        SONIC_DISRUPTION,
        DUST_CLOUD,
        ARROW_TRAIL,
        SHIELD,
        DELAY,
        IMBUE_FIZZLE,
        IMBUE,
        ROTATE,
        SELF,
        SELF_FIZZLE,
        PROJECTILE,
        FEY
    )
}