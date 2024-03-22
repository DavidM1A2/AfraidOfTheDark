package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.PlayerChangePowerSourceEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.world.entity.player.Player
import kotlin.reflect.KClass

class PlayerChangePowerSourceResearchTrigger :
    ResearchTrigger<PlayerChangePowerSourceEvent, PlayerChangePowerSourceResearchTriggerConfig>(PlayerChangePowerSourceResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "player_change_power_source")
    }

    override fun getEventType(config: PlayerChangePowerSourceResearchTriggerConfig): KClass<PlayerChangePowerSourceEvent> {
        return PlayerChangePowerSourceEvent::class
    }

    override fun getAffectedPlayer(event: PlayerChangePowerSourceEvent, config: PlayerChangePowerSourceResearchTriggerConfig): Player? {
        return event.player
    }

    override fun shouldUnlock(player: Player, event: PlayerChangePowerSourceEvent, config: PlayerChangePowerSourceResearchTriggerConfig): Boolean {
        if (config.newPowerSource != null && event.newPowerSource != config.newPowerSource) {
            return false
        }

        if (config.oldPowerSource != null && event.oldPowerSource != config.oldPowerSource) {
            return false
        }

        return true
    }
}