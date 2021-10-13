package com.davidm1a2.afraidofthedark.common.research.trigger.base

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.mojang.serialization.Codec
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.eventbus.api.Event
import kotlin.reflect.KClass

class ConfiguredResearchTrigger<E : Event, C : ResearchTriggerConfig, T : ResearchTrigger<E, C>>(
    private val trigger: T,
    val config: C
) {
    fun getEventType(): KClass<E> {
        return trigger.getEventType(config)
    }

    fun getAffectedPlayer(event: E): PlayerEntity? {
        return trigger.getAffectedPlayer(event, config)
    }

    fun shouldUnlock(player: PlayerEntity, event: E): Boolean {
        return trigger.shouldUnlock(player, event, config)
    }

    companion object {
        val CODEC: Codec<ConfiguredResearchTrigger<*, *, *>> = ResourceLocation.CODEC.dispatch({ it.trigger.registryName }, {
            ModRegistries.RESEARCH_TRIGGERS.getValue(it)!!.configurationCodec
        })
    }
}