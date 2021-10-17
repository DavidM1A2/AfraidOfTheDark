package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.research.trigger.BlockBrokenResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.InBiomeResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.ItemCraftedResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.ItemSmeltedResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.KillEntityResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.ManualResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.MultiplicityResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.PlayerCastResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.PlayerHurtResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.StartedAOTDResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.UseBlockResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.UseItemResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.ZoneEnteredResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.ZoneExitedResearchTrigger

object ModResearchTriggers {
    val PLAYER_HURT = PlayerHurtResearchTrigger()
    val PLAYER_CAST = PlayerCastResearchTrigger()
    val BLOCK_BROKEN = BlockBrokenResearchTrigger()
    val ITEM_CRAFTED = ItemCraftedResearchTrigger()
    val ITEM_SMELTED = ItemSmeltedResearchTrigger()
    val USE_BLOCK = UseBlockResearchTrigger()
    val USE_ITEM = UseItemResearchTrigger()
    val KILL_ENTITY = KillEntityResearchTrigger()
    val START_AOTD = StartedAOTDResearchTrigger()
    val MANUAL = ManualResearchTrigger()
    val ENTER_ZONE = ZoneEnteredResearchTrigger()
    val EXIT_ZONE = ZoneExitedResearchTrigger()
    val IN_BIOME = InBiomeResearchTrigger()
    val MULTIPLICITY = MultiplicityResearchTrigger()

    val LIST = arrayOf(
        PLAYER_HURT,
        PLAYER_CAST,
        BLOCK_BROKEN,
        ITEM_CRAFTED,
        ITEM_SMELTED,
        USE_BLOCK,
        USE_ITEM,
        KILL_ENTITY,
        START_AOTD,
        MANUAL,
        ENTER_ZONE,
        EXIT_ZONE,
        IN_BIOME,
        MULTIPLICITY
    )
}