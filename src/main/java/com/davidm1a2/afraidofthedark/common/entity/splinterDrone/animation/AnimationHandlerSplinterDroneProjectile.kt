package com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode

/**
 * Animation handler class for the splinter drone projectile
 */
class AnimationHandlerSplinterDroneProjectile : AnimationHandler(
    setOf(
        ChannelSping("Sping", 100.0f, 100, ChannelMode.LINEAR)
    )
)