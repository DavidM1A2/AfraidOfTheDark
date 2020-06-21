package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.effect.*

/**
 * A static class containing all of our spell effect references for us
 */
object ModSpellEffects {
    val DIG = SpellEffectDig()
    val EXPLOSION = SpellEffectExplosion()
    val TELEPORT = SpellEffectTeleport()
    val GROW = SpellEffectGrow()
    val HEAL = SpellEffectHeal()
    val FEED = SpellEffectFeed()
    val BURN = SpellEffectBurn()
    val POTION_EFFECT = SpellEffectPotionEffect()
    val SMOKE_SCREEN = SpellEffectSmokeScreen()
    val CLEANSE = SpellEffectCleanse()
    val EXTINGUISH = SpellEffectExtinguish()
    val ENDER_POCKET = SpellEffectEnderPocket()
    val FREEZE = SpellEffectFreeze()
    val CHARM = SpellEffectCharm()

    // An array containing a list of spell effects that AOTD adds
    val SPELL_EFFECTS = arrayOf(
        DIG,
        EXPLOSION,
        TELEPORT,
        GROW,
        HEAL,
        FEED,
        BURN,
        POTION_EFFECT,
        SMOKE_SCREEN,
        CLEANSE,
        EXTINGUISH,
        ENDER_POCKET,
        FREEZE,
        CHARM
    )
}