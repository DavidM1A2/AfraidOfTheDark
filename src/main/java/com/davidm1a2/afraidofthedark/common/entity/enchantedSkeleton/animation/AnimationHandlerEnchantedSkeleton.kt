/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode

/**
 * Animation handler class for the enchanted skeleton
 */
class AnimationHandlerEnchantedSkeleton : AnimationHandler(
    setOf(
        ChannelWalk("Walk", 20.0f, 40, ChannelMode.LINEAR),
        ChannelAttack("Attack", 30.0f, 20, ChannelMode.LINEAR),
        ChannelSpawn("Spawn", 20.0f, 40, ChannelMode.LINEAR),
        ChannelIdle("Idle", 10.0f, 20, ChannelMode.LOOP)
    )
)