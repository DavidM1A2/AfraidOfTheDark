package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import kotlin.reflect.KClass

class ManualResearchTrigger : ResearchTrigger<ManualResearchTriggerEvent, ManualResearchTriggerConfig>(ManualResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "manual")
    }

    override fun getEventType(config: ManualResearchTriggerConfig): KClass<ManualResearchTriggerEvent> {
        return ManualResearchTriggerEvent::class
    }

    override fun getAffectedPlayer(event: ManualResearchTriggerEvent, config: ManualResearchTriggerConfig): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: ManualResearchTriggerEvent, config: ManualResearchTriggerConfig): Boolean {
        return event.research == config.research
    }
}