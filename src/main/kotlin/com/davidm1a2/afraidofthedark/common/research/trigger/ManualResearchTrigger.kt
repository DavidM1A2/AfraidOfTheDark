package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import kotlin.reflect.KClass

class ManualResearchTrigger : ResearchTrigger<ManualResearchTriggerEvent, ManualResearchTriggerConfig>(ManualResearchTriggerConfig.CODEC) {
    override val type: KClass<ManualResearchTriggerEvent> = ManualResearchTriggerEvent::class

    init {
        setRegistryName(Constants.MOD_ID, "manual")
    }

    override fun getAffectedPlayer(event: ManualResearchTriggerEvent): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: ManualResearchTriggerEvent, config: ManualResearchTriggerConfig): Boolean {
        return event.research == config.research
    }
}