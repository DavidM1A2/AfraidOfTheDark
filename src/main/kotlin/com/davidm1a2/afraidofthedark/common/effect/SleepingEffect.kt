package com.davidm1a2.afraidofthedark.common.effect

import com.davidm1a2.afraidofthedark.common.effect.base.AOTDEffect
import net.minecraft.potion.EffectType
import java.awt.Color

/**
 * Sleeping potion makes players 'drowsy'
 *
 * @constructor just sets item properties
 */
class SleepingEffect : AOTDEffect("sleeping_potion", EffectType.NEUTRAL, Color(255, 255, 255))
