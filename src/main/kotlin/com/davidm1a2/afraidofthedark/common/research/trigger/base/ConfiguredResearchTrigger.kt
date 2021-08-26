package com.davidm1a2.afraidofthedark.common.research.trigger.base

import com.davidm1a2.afraidofthedark.common.constants.ModResearchTriggers
import com.mojang.serialization.Codec
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.eventbus.api.Event

class ConfiguredResearchTrigger<E : Event, C : ResearchTriggerConfig, T : ResearchTrigger<E, C>>(
    val trigger: T,
    val config: C
) {
    fun getAffectedPlayer(event: E): PlayerEntity? {
        return trigger.getAffectedPlayer(event)
    }

    fun shouldUnlock(player: PlayerEntity, event: E): Boolean {
        return trigger.shouldUnlock(player, event, config)
    }

    companion object {
        val CODEC: Codec<ConfiguredResearchTrigger<*, *, *>> = ResourceLocation.CODEC.dispatch({ it.trigger.registryName }, {
            // TODO: How can we pull from the registry before it is filled? :/ This is a hack but it works for now
            // ModRegistries.RESEARCH_TRIGGERS.getValue(it)!!.configuredCodec
            ModResearchTriggers.NAME_TO_TRIGGER[it]!!.configurationCodec
        })
    }
}