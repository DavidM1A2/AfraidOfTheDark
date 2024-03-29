package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerStartedAfraidOfTheDarkEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.NoResearchTriggerConfig
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import kotlin.reflect.KClass

class StartedAOTDResearchTrigger :
    ResearchTrigger<PlayerStartedAfraidOfTheDarkEvent, NoResearchTriggerConfig>(NoResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "started_aotd")
    }

    override fun getEventType(config: NoResearchTriggerConfig): KClass<PlayerStartedAfraidOfTheDarkEvent> {
        return PlayerStartedAfraidOfTheDarkEvent::class
    }

    override fun getAffectedPlayer(event: PlayerStartedAfraidOfTheDarkEvent, config: NoResearchTriggerConfig): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: PlayerStartedAfraidOfTheDarkEvent, config: NoResearchTriggerConfig): Boolean {
        return true
    }
}