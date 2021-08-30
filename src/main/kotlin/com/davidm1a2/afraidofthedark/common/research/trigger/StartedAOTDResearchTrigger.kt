package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerStartedAfraidOfTheDarkEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import kotlin.reflect.KClass

class StartedAOTDResearchTrigger :
    ResearchTrigger<PlayerStartedAfraidOfTheDarkEvent, StartedAOTDResearchTriggerConfig>(StartedAOTDResearchTriggerConfig.CODEC) {
    override val type: KClass<PlayerStartedAfraidOfTheDarkEvent> = PlayerStartedAfraidOfTheDarkEvent::class

    init {
        setRegistryName(Constants.MOD_ID, "started_aotd")
    }

    override fun getAffectedPlayer(event: PlayerStartedAfraidOfTheDarkEvent): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: PlayerStartedAfraidOfTheDarkEvent, config: StartedAOTDResearchTriggerConfig): Boolean {
        return true
    }
}