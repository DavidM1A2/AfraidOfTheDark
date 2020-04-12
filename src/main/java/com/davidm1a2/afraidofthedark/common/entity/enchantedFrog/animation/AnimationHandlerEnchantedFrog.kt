package com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode

/**
 * Animation handler class for the magic frog entity
 */
class AnimationHandlerEnchantedFrog : AnimationHandler(
    setOf(
        ChannelHop("hop", 120.0f, 80, ChannelMode.LINEAR),
        ChannelCast("cast", 120.0f, 60, ChannelMode.LINEAR)
    )
)