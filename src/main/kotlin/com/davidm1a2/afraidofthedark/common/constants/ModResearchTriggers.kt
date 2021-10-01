package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.research.trigger.*

object ModResearchTriggers {
    val PLAYER_HURT = PlayerHurtResearchTrigger()
    val PLAYER_CAST = PlayerCastResearchTrigger()
    val BLOCK_BROKEN = BlockBrokenResearchTrigger()
    val ITEM_CRAFTED = ItemCraftedResearchTrigger()
    val USE_BLOCK = UseBlockResearchTrigger()
    val USE_ITEM = UseItemResearchTrigger()
    val KILL_ENTITY = KillEntityResearchTrigger()
    val START_AOTD = StartedAOTDResearchTrigger()
    val MANUAL = ManualResearchTrigger()
    val ENTER_ZONE = ZoneEnteredResearchTrigger()
    val EXIT_ZONE = ZoneExitedResearchTrigger()
    val IN_BIOME = InBiomeResearchTrigger()

    val LIST = arrayOf(
        PLAYER_HURT,
        PLAYER_CAST,
        BLOCK_BROKEN,
        ITEM_CRAFTED,
        USE_BLOCK,
        USE_ITEM,
        KILL_ENTITY,
        START_AOTD,
        MANUAL,
        ENTER_ZONE,
        EXIT_ZONE,
        IN_BIOME
    )
}