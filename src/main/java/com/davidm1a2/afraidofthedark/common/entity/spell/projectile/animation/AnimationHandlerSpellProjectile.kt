package com.davidm1a2.afraidofthedark.common.entity.spell.projectile.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode

/**
 * Animation handler class for the spell projectile
 */
class AnimationHandlerSpellProjectile : AnimationHandler(
    setOf(
        ChannelSpellProjectileIdle("Idle", 100.0f, 60, ChannelMode.LOOP)
    )
)