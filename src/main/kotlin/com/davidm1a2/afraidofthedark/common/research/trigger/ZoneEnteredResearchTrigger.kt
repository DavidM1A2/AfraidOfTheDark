package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerEnterZoneEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import kotlin.reflect.KClass

class ZoneEnteredResearchTrigger : ResearchTrigger<PlayerEnterZoneEvent, ZoneEnteredResearchTriggerConfig>(ZoneEnteredResearchTriggerConfig.CODEC) {
    override val type: KClass<PlayerEnterZoneEvent> = PlayerEnterZoneEvent::class

    init {
        setRegistryName(Constants.MOD_ID, "zone_entered")
    }

    override fun getAffectedPlayer(event: PlayerEnterZoneEvent): PlayerEntity? {
        return event.player
    }

    override fun shouldUnlock(player: PlayerEntity, event: PlayerEnterZoneEvent, config: ZoneEnteredResearchTriggerConfig): Boolean {
        return event.tileEntity == config.tileEntityType.registryName
    }
}