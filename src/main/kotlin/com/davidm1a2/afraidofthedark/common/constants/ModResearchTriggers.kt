package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.research.trigger.PlayerHurtResearchTrigger

object ModResearchTriggers {
    val PLAYER_HURT = PlayerHurtResearchTrigger()

    val LIST = arrayOf(
        PLAYER_HURT
    )

    val NAME_TO_TRIGGER = LIST.associateBy { it.registryName }
}