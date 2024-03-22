package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerEnterZoneEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.world.entity.player.Player
import kotlin.reflect.KClass

class ZoneEnteredResearchTrigger : ResearchTrigger<PlayerEnterZoneEvent, ZoneEnteredResearchTriggerConfig>(ZoneEnteredResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "zone_entered")
    }

    override fun getEventType(config: ZoneEnteredResearchTriggerConfig): KClass<PlayerEnterZoneEvent> {
        return PlayerEnterZoneEvent::class
    }

    override fun getAffectedPlayer(event: PlayerEnterZoneEvent, config: ZoneEnteredResearchTriggerConfig): Player? {
        return event.player
    }

    override fun shouldUnlock(player: Player, event: PlayerEnterZoneEvent, config: ZoneEnteredResearchTriggerConfig): Boolean {
        return event.tileEntity == config.tileEntityType
    }
}