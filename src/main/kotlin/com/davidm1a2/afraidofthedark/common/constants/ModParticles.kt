package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.particle.DigParticleType
import com.davidm1a2.afraidofthedark.common.particle.EnariaFightEventParticleType
import com.davidm1a2.afraidofthedark.common.particle.EnariasAltarParticleType
import com.davidm1a2.afraidofthedark.common.particle.EnchantedFrogSpawnParticleType
import com.davidm1a2.afraidofthedark.common.particle.EnderParticleType
import com.davidm1a2.afraidofthedark.common.particle.ExplosionParticleType
import com.davidm1a2.afraidofthedark.common.particle.FireParticleType
import com.davidm1a2.afraidofthedark.common.particle.FlyParticleType
import com.davidm1a2.afraidofthedark.common.particle.FreezeParticleType
import com.davidm1a2.afraidofthedark.common.particle.GrowParticleType
import com.davidm1a2.afraidofthedark.common.particle.HealParticleType
import com.davidm1a2.afraidofthedark.common.particle.PoisonParticleType
import com.davidm1a2.afraidofthedark.common.particle.SmokeScreenParticleType
import com.davidm1a2.afraidofthedark.common.particle.SpellCast2ParticleType
import com.davidm1a2.afraidofthedark.common.particle.SpellCast3ParticleType
import com.davidm1a2.afraidofthedark.common.particle.SpellCastParticleType
import com.davidm1a2.afraidofthedark.common.particle.SpellHitParticleType
import com.davidm1a2.afraidofthedark.common.particle.SpellLaserParticleType
import com.davidm1a2.afraidofthedark.common.particle.StrengthParticleType
import com.davidm1a2.afraidofthedark.common.particle.WeaknessParticleType

object ModParticles {
    val ENARIAS_ALTAR = EnariasAltarParticleType()
    val ENARIA_FIGHT_EVENT = EnariaFightEventParticleType()
    val ENCHANTED_FROG_SPAWN = EnchantedFrogSpawnParticleType()
    val SMOKE_SCREEN = SmokeScreenParticleType()
    val SPELL_CAST = SpellCastParticleType()
    val SPELL_CAST2 = SpellCast2ParticleType()
    val SPELL_CAST3 = SpellCast3ParticleType()
    val SPELL_HIT = SpellHitParticleType()
    val SPELL_LASER = SpellLaserParticleType()
    val FREEZE = FreezeParticleType()
    val DIG = DigParticleType()
    val ENDER = EnderParticleType()
    val EXPLOSION = ExplosionParticleType()
    val FIRE = FireParticleType()
    val FLY = FlyParticleType()
    val GROW = GrowParticleType()
    val HEAL = HealParticleType()
    val POISON = PoisonParticleType()
    val STRENGTH = StrengthParticleType()
    val WEAKNESS = WeaknessParticleType()

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
        WEAKNESS
    )
}