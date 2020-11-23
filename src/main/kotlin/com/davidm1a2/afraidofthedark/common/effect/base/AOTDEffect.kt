package com.davidm1a2.afraidofthedark.common.effect.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.potion.Effect
import net.minecraft.potion.EffectType
import java.awt.Color

/**
 * Base class for all AOTD potions
 *
 * @constructor sets item properties
 * @param name The name of the potion in the game registry
 * @param effectType   The potion effect type
 * @param color        The color of the potion
 */
open class AOTDEffect(name: String, effectType: EffectType, color: Color) : Effect(effectType, color.hashCode()) {
    init {
        this.setRegistryName(Constants.MOD_ID, name)
    }

    /**
     * True if the duration is greater than 0
     *
     * @param duration  The duration left on the drink
     * @param amplifier The potion amplifier
     * @return True if the potion is ready, false otherwise
     */
    override fun isReady(duration: Int, amplifier: Int): Boolean {
        return duration >= 1
    }
}