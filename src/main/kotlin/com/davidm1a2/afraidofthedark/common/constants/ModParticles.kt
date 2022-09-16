package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.particle.CleanseParticleType
import com.davidm1a2.afraidofthedark.common.particle.base.BasicAOTDParticleType

object ModParticles {
    val ENARIAS_ALTAR = BasicAOTDParticleType("enarias_altar")
    val ENARIA_FIGHT_EVENT = BasicAOTDParticleType("enaria_fight_event")
    val ENCHANTED_FROG_SPAWN = BasicAOTDParticleType("enchanted_frog_spawn")
    val SMOKE_SCREEN = BasicAOTDParticleType("smoke_screen", true)
    val SPELL_CAST = BasicAOTDParticleType("spell_cast")
    val SPELL_CAST2 = BasicAOTDParticleType("spell_cast2")
    val SPELL_CAST3 = BasicAOTDParticleType("spell_cast3")
    val SPELL_HIT = BasicAOTDParticleType("spell_hit")
    val SPELL_LASER = BasicAOTDParticleType("spell_laser")
    val FREEZE = BasicAOTDParticleType("freeze")
    val DIG = BasicAOTDParticleType("dig")
    val ENDER = BasicAOTDParticleType("ender")
    val EXPLOSION = BasicAOTDParticleType("explosion")
    val FIRE = BasicAOTDParticleType("fire")
    val FLY = BasicAOTDParticleType("fly")
    val GROW = BasicAOTDParticleType("grow")
    val HEAL = BasicAOTDParticleType("heal")
    val POISON = BasicAOTDParticleType("poison")
    val STRENGTH = BasicAOTDParticleType("strength")
    val WEAKNESS = BasicAOTDParticleType("weakness")
    val VITAE_EXTRACTOR_BURN = BasicAOTDParticleType("vitae_extractor_burn")
    val VITAE_EXTRACTOR_CHARGE = BasicAOTDParticleType("vitae_extractor_charge")
    val WARD = BasicAOTDParticleType("ward")
    val CLEANSE = CleanseParticleType()
    val DISINTEGRATE = BasicAOTDParticleType("disintegrate")
    val FROST_PHOENIX_STORM = BasicAOTDParticleType("frost_phoenix_storm")

    val PARTICLE_LIST = arrayOf(
        ENARIAS_ALTAR,
        ENARIA_FIGHT_EVENT,
        ENCHANTED_FROG_SPAWN,
        SMOKE_SCREEN,
        SPELL_CAST,
        SPELL_CAST2,
        SPELL_CAST3,
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
        FROST_PHOENIX_STORM
    )
}