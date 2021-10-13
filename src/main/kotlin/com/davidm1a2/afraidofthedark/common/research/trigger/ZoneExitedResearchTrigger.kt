package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerExitZoneEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import kotlin.reflect.KClass

class ZoneExitedResearchTrigger : ResearchTrigger<PlayerExitZoneEvent, ZoneExitedResearchTriggerConfig>(ZoneExitedResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "zone_exited")
    }

    override fun getEventType(config: ZoneExitedResearchTriggerConfig): KClass<PlayerExitZoneEvent> {
        return PlayerExitZoneEvent::class
    }

    override fun getAffectedPlayer(event: PlayerExitZoneEvent, config: ZoneExitedResearchTriggerConfig): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: PlayerExitZoneEvent, config: ZoneExitedResearchTriggerConfig): Boolean {
        return event.tileEntity == config.tileEntityType
    }
}