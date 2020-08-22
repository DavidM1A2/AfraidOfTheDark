package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.particle.*

object ModParticles {
    val ENARIA_BASIC_ATTACK = EnariaBasicAttackParticleType()
    val ENARIAS_ALTAR = EnariasAltarParticleType()
    val ENARIA_SPELL_CAST = EnariaSpellCastParticleType()
    val ENARIA_SPELL_CAST_2 = EnariaSpellCast2ParticleType()
    val ENARIA_TELEPORT = EnariaTeleportParticleType()
    val ENCHANTED_FROG_SPAWN = EnchantedFrogSpawnParticleType()
    val SMOKE_SCREEN = SmokeScreenParticleType()
    val SPELL_CAST = SpellCastParticleType()
    val SPELL_HIT = SpellHitParticleType()
    val SPELL_LASER = SpellLaserParticleType()

    val PARTICLE_LIST = arrayOf(
        ENARIA_BASIC_ATTACK,
        ENARIAS_ALTAR,
        ENARIA_SPELL_CAST,
        ENARIA_SPELL_CAST_2,
        ENARIA_TELEPORT,
        ENCHANTED_FROG_SPAWN,
        SMOKE_SCREEN,
        SPELL_CAST,
        SPELL_HIT,
        SPELL_LASER
    )
}