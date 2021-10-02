package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.custom.CastSpellEvent
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import kotlin.reflect.KClass

class PlayerCastResearchTrigger : ResearchTrigger<CastSpellEvent, PlayerCastResearchTriggerConfig>(PlayerCastResearchTriggerConfig.CODEC) {
    override val type: KClass<CastSpellEvent> = CastSpellEvent::class

    init {
        setRegistryName(Constants.MOD_ID, "player_cast")
    }

    override fun getAffectedPlayer(event: CastSpellEvent): PlayerEntity? {
        return event.entity as? PlayerEntity
    }

    override fun shouldUnlock(player: PlayerEntity, event: CastSpellEvent, config: PlayerCastResearchTriggerConfig): Boolean {
        if (config.powerSource == null || config.powerSource == event.spell.powerSource) {
            if (config.deliveryMethod == null || event.spell.getAllDeliveryMethods().contains(config.deliveryMethod)) {
                if (config.effect == null || event.spell.getAllEffects().contains(config.effect)) {
                    if (config.minCost == null || config.minCost <= event.spell.getCost()) {
                        return true
                    }
                }
            }
        }
        return false
    }
}