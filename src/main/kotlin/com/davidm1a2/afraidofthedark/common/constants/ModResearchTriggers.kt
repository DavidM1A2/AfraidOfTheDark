package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.research.trigger.BlockBrokenResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.ItemCraftedResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.PlayerHurtResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.UseBlockResearchTrigger

object ModResearchTriggers {
    val PLAYER_HURT = PlayerHurtResearchTrigger()
    val BLOCK_BROKEN = BlockBrokenResearchTrigger()
    val ITEM_CRAFTED = ItemCraftedResearchTrigger()
    val RIGHT_CLICK_BLOCK = UseBlockResearchTrigger()

    val LIST = arrayOf(
        PLAYER_HURT,
        BLOCK_BROKEN,
        ITEM_CRAFTED,
        RIGHT_CLICK_BLOCK
    )
}