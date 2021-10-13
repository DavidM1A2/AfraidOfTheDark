package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.eventbus.api.Event
import kotlin.reflect.KClass

class MultiplicityResearchTrigger : ResearchTrigger<Event, MultiplicityResearchTriggerConfig>(MultiplicityResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "multiplicity")
    }

    override fun getEventType(config: MultiplicityResearchTriggerConfig): KClass<Event> {
        return config.baseTrigger.getEventType()
    }

    override fun getAffectedPlayer(event: Event, config: MultiplicityResearchTriggerConfig): PlayerEntity? {
        return config.baseTrigger.getAffectedPlayer(event)
    }

    override fun shouldUnlock(player: PlayerEntity, event: Event, config: MultiplicityResearchTriggerConfig): Boolean {
        val wouldUnlock = config.baseTrigger.shouldUnlock(player, event)
        if (!wouldUnlock) {
            return false
        }

        val playerBasics = player.getBasics()
        val currentMultiplicity = playerBasics.getMultiplicity(config.key)
        val newMultiplicity = currentMultiplicity + 1
        playerBasics.setMultiplicity(config.key, newMultiplicity)
        return newMultiplicity >= config.times
    }
}