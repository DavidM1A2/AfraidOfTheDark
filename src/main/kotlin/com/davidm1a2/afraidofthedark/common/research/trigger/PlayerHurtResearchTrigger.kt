package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTrigger

class PlayerHurtResearchTrigger : ResearchTrigger<PlayerHurtResearchTriggerConfig>(PlayerHurtResearchTriggerConfig.CODEC) {
    init {
        setRegistryName(Constants.MOD_ID, "player_hurt")
    }

    /*
    TODO: Add a real implementation

    private fun procTrigger(trigger: JsonObject, research: Research) {
        when (JSONUtils.getAsString(trigger, "type")) {
            "takeDamage" -> {
                val fromEntity = JSONUtils.getAsString(trigger, "entity")
                AfraidOfTheDark.researchHooks.addHook(LivingDamageEvent::class) {
                    val event = it as LivingDamageEvent
                    if (event.entity is PlayerEntity) {
                        val player = event.entity as PlayerEntity
                        val playerResearch = player.getResearch()
                        if (playerResearch.canResearch(research)) {
                            if (event.source.entity != null) {
                                if (event.source.entity!!.type.registryName.toString() == fromEntity) {
                                    if (!JSONUtils.getAsBoolean(trigger, "mustSurvive") || player.health > event.amount) {
                                        playerResearch.setResearch(research, true)
                                        playerResearch.sync(player, true)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
     */
}