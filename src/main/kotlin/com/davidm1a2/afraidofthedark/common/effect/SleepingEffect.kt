package com.davidm1a2.afraidofthedark.common.effect

import com.davidm1a2.afraidofthedark.common.effect.base.AOTDEffect
import net.minecraft.world.effect.MobEffectCategory
import java.awt.Color

/**
 * Sleeping potion makes players 'drowsy'
 *
 * @constructor just sets item properties
 */
class SleepingEffect : AOTDEffect("sleeping", MobEffectCategory.NEUTRAL, Color(255, 255, 255))
