package com.davidm1a2.afraidofthedark.common.entity.enaria.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode

/**
 * Animation handler class for the ghastly enaria entity
 */
class AnimationHandlerGhastlyEnaria :
    AnimationHandler(setOf(ChannelDance("dance", 30.0f, 300, ChannelMode.LINEAR)))