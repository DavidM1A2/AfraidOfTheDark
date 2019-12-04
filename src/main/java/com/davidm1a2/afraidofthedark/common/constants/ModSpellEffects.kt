package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.spell.component.effect.*
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry
import net.minecraft.util.ResourceLocation

/**
 * A static class containing all of our spell effect references for us
 */
object ModSpellEffects
{
    val DIG = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "dig")) { SpellEffectDig() }
    val EXPLOSION = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "explosion")) { SpellEffectExplosion() }
    val TELEPORT = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "teleport")) { SpellEffectTeleport() }
    val GROW = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "grow")) { SpellEffectGrow() }
    val HEAL = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "heal")) { SpellEffectHeal() }
    val FEED = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "feed")) { SpellEffectFeed() }
    val BURN = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "burn")) { SpellEffectBurn() }
    val POTION_EFFECT = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "potion_effect")) { SpellEffectPotionEffect() }
    val SMOKE_SCREEN = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "smoke_screen")) { SpellEffectSmokeScreen() }
    val CLEANSE = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "cleanse")) { SpellEffectCleanse() }
    val EXTINGUISH = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "extinguish")) { SpellEffectExtinguish() }
    val ENDER_POCKET = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "ender_pocket")) { SpellEffectEnderPocket() }
    val FREEZE = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "freeze")) { SpellEffectFreeze() }
    val CHARM = SpellEffectEntry(ResourceLocation(Constants.MOD_ID, "charm")) { SpellEffectCharm() }

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