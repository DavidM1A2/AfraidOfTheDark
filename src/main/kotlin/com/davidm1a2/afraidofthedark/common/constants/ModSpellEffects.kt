package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.effect.*

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
    val POTION_EFFECT = PotionEffectSpellEffect()
    val SMOKE_SCREEN = SmokeScreenSpellEffect()
    val CLEANSE = CleanseSpellEffect()
    val EXTINGUISH = ExtinguishSpellEffect()
    val ENDER_POCKET = EnderPocketSpellEffect()
    val FREEZE = FreezeSpellEffect()
    val CHARM = CharmSpellEffect()

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