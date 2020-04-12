package com.davidm1a2.afraidofthedark.common.entity.enaria.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode

/**
 * Animation handler class for the enaria entity
 */
class AnimationHandlerEnaria : AnimationHandler(
    setOf(
        ChannelWalk("walk", 59.0f, 59, ChannelMode.LINEAR),
        ChannelArmthrow("armthrow", 61.0f, 61, ChannelMode.LINEAR),
        ChannelAutoattack("autoattack", 70.0f, 51, ChannelMode.LINEAR),
        ChannelSpell("spell", 90.0f, 121, ChannelMode.LINEAR)
    )
)