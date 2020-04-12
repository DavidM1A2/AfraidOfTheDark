package com.davidm1a2.afraidofthedark.common.entity.werewolf.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode

/**
 * Animation handler class for the werewolf
 */
class AnimationHandlerWerewolf : AnimationHandler(
    setOf(
        ChannelBite("Bite", 50.0f, 21, ChannelMode.LINEAR),
        ChannelRun("Run", 60.0f, 32, ChannelMode.LINEAR)
    )
)