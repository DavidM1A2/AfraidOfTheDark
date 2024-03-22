package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.CastSpellEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.world.entity.player.Player
import kotlin.reflect.KClass

class PlayerCastResearchTrigger : ResearchTrigger<CastSpellEvent, PlayerCastResearchTriggerConfig>(PlayerCastResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "player_cast")
    }

    override fun getEventType(config: PlayerCastResearchTriggerConfig): KClass<CastSpellEvent> {
        return CastSpellEvent::class
    }

    override fun getAffectedPlayer(event: CastSpellEvent, config: PlayerCastResearchTriggerConfig): Player? {
        return event.entity as? Player
    }

    override fun shouldUnlock(player: Player, event: CastSpellEvent, config: PlayerCastResearchTriggerConfig): Boolean {
        if (config.powerSource == null || config.powerSource == event.powerSourceUsed) {
            if (config.deliveryMethod == null || event.spell.hasDeliveryMethod(config.deliveryMethod!!)) {
                if (config.effect == null || event.spell.hasEffect(config.effect!!)) {
                    if (config.minCost == null || config.minCost <= event.spell.getCost()) {
                        return true
                    }
                }
            }
        }
        return false
    }
}