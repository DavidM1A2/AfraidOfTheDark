package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.event.entity.living.LivingDamageEvent
import kotlin.reflect.KClass

class PlayerHurtResearchTrigger : ResearchTrigger<LivingDamageEvent, PlayerHurtResearchTriggerConfig>(PlayerHurtResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "player_hurt")
    }

    override fun getEventType(config: PlayerHurtResearchTriggerConfig): KClass<LivingDamageEvent> {
        return LivingDamageEvent::class
    }

    override fun getAffectedPlayer(event: LivingDamageEvent, config: PlayerHurtResearchTriggerConfig): PlayerEntity? {
        return event.entity as? PlayerEntity
    }

    override fun shouldUnlock(player: PlayerEntity, event: LivingDamageEvent, config: PlayerHurtResearchTriggerConfig): Boolean {
        if (event.source.entity?.type == config.attackingEntityType) {
            if (!config.mustSurvive || player.health > event.amount) {
                return true
            }
        }
        return false
    }
}