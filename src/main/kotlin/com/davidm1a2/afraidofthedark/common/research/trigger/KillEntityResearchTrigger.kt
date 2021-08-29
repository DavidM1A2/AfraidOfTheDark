package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.EntityDamageSource
import net.minecraftforge.event.entity.living.LivingDeathEvent
import kotlin.reflect.KClass

class KillEntityResearchTrigger : ResearchTrigger<LivingDeathEvent, KillEntityResearchTriggerConfig>(KillEntityResearchTriggerConfig.CODEC) {
    override val type: KClass<LivingDeathEvent> = LivingDeathEvent::class

    init {
        setRegistryName(Constants.MOD_ID, "kill_entity")
    }

    override fun getAffectedPlayer(event: LivingDeathEvent): PlayerEntity? {
        val source = event.source
        if (source is EntityDamageSource) {
            val entity = source.entity
            if (entity is PlayerEntity) {
                return entity
            }
        }
        return null
    }

    override fun shouldUnlock(player: PlayerEntity, event: LivingDeathEvent, config: KillEntityResearchTriggerConfig): Boolean {
        return event.entity.type == config.entityType
    }
}