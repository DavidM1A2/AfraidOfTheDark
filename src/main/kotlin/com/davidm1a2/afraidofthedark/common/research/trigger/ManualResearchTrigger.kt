package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.world.entity.player.Player
import kotlin.reflect.KClass

class ManualResearchTrigger : ResearchTrigger<ManualResearchTriggerEvent, ManualResearchTriggerConfig>(ManualResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "manual")
    }

    override fun getEventType(config: ManualResearchTriggerConfig): KClass<ManualResearchTriggerEvent> {
        return ManualResearchTriggerEvent::class
    }

    override fun getAffectedPlayer(event: ManualResearchTriggerEvent, config: ManualResearchTriggerConfig): Player? {
        return event.player
    }

    override fun shouldUnlock(player: Player, event: ManualResearchTriggerEvent, config: ManualResearchTriggerConfig): Boolean {
        return event.research == config.research
    }
}