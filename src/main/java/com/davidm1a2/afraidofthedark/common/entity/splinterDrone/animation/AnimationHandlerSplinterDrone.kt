package com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode

/**
 * Animation handler class for the splinter drone
 */
class AnimationHandlerSplinterDrone : AnimationHandler(
    setOf(
        ChannelActivate("Activate", 25.0f, 100, ChannelMode.LINEAR),
        ChannelCharge("Charge", 100.0f, 100, ChannelMode.LINEAR),
        ChannelIdle("Idle", 25.0f, 100, ChannelMode.LINEAR)
    )
)