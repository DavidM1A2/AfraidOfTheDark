package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.research.trigger.BlockBrokenResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.ItemCraftedResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.KillEntityResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.PlayerHurtResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.StartedAOTDResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.UseBlockResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.UseItemResearchTrigger

object ModResearchTriggers {
    val PLAYER_HURT = PlayerHurtResearchTrigger()
    val BLOCK_BROKEN = BlockBrokenResearchTrigger()
    val ITEM_CRAFTED = ItemCraftedResearchTrigger()
    val USE_BLOCK = UseBlockResearchTrigger()
    val USE_ITEM = UseItemResearchTrigger()
    val KILL_ENTITY = KillEntityResearchTrigger()
    val START_AOTD = StartedAOTDResearchTrigger()

    val LIST = arrayOf(
        PLAYER_HURT,
        BLOCK_BROKEN,
        ITEM_CRAFTED,
        USE_BLOCK,
        USE_ITEM,
        KILL_ENTITY,
        START_AOTD
    )
}