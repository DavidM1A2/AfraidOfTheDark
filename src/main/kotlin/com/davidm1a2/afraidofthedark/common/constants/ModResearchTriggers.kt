package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.research.trigger.BlockBrokenResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.PlayerHurtResearchTrigger

object ModResearchTriggers {
    val PLAYER_HURT = PlayerHurtResearchTrigger()
    val BLOCK_BROKEN = BlockBrokenResearchTrigger()

    val LIST = arrayOf(
        PLAYER_HURT,
        BLOCK_BROKEN
    )
}