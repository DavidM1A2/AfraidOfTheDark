package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.effect.BurnSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.CharmSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.CleanseSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.DigSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.DisintegrateSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.EnderPocketSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.ExplosionSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.ExtinguishSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.FeedSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.FeyLightSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.FreezeSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.GrowSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.HealSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.LightningSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.PushSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.SmokeScreenSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.SonicDisruptionSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.SpeedSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.SummonArrowEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.TeleportSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.WardSpellEffect

/**
 * A static class containing all of our spell effect references for us
 */
object ModSpellEffects {
    val DIG = DigSpellEffect()
    val EXPLOSION = ExplosionSpellEffect()
    val TELEPORT = TeleportSpellEffect()
    val GROW = GrowSpellEffect()
    val HEAL = HealSpellEffect()
    val FEED = FeedSpellEffect()
    val BURN = BurnSpellEffect()
    val SMOKE_SCREEN = SmokeScreenSpellEffect()
    val CLEANSE = CleanseSpellEffect()
    val EXTINGUISH = ExtinguishSpellEffect()
    val ENDER_POCKET = EnderPocketSpellEffect()
    val FREEZE = FreezeSpellEffect()
    val CHARM = CharmSpellEffect()
    val SPEED = SpeedSpellEffect()
    val DISINTEGRATE = DisintegrateSpellEffect()
    val PUSH = PushSpellEffect()
    val LIGHTNING = LightningSpellEffect()
    val WARD = WardSpellEffect()
    val SONIC_DISRUPTION = SonicDisruptionSpellEffect()
    val SUMMON_ARROW = SummonArrowEffect()
    val FEY_LIGHT = FeyLightSpellEffect()

    // An array containing a list of spell effects that AOTD adds
    val SPELL_EFFECTS = arrayOf(
        DIG,
        EXPLOSION,
        TELEPORT,
        GROW,
        HEAL,
        FEED,
        BURN,
        SMOKE_SCREEN,
        CLEANSE,
        EXTINGUISH,
        ENDER_POCKET,
        FREEZE,
        CHARM,
        SPEED,
        DISINTEGRATE,
        PUSH,
        LIGHTNING,
        WARD,
        SONIC_DISRUPTION,
        SUMMON_ARROW,
        FEY_LIGHT
    )
}