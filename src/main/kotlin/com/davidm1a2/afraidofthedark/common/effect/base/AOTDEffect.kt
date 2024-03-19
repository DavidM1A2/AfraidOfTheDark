package com.davidm1a2.afraidofthedark.common.effect.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import java.awt.Color

/**
 * Base class for all AOTD potions
 *
 * @constructor sets item properties
 * @param name The name of the potion in the game registry
 * @param effectType   The potion effect type
 * @param color        The color of the potion
 */
open class AOTDEffect(name: String, effectType: MobEffectCategory, color: Color) : MobEffect(effectType, color.hashCode()) {
    init {
        this.setRegistryName(Constants.MOD_ID, name)
    }
}