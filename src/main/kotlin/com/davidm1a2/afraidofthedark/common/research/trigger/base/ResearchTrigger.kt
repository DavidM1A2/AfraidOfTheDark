package com.davidm1a2.afraidofthedark.common.research.trigger.base

import com.mojang.serialization.Codec
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.registries.ForgeRegistryEntry
import kotlin.reflect.KClass

abstract class ResearchTrigger<E : Event, C : ResearchTriggerConfig>(codec: Codec<C>) : ForgeRegistryEntry<ResearchTrigger<*, *>>() {
    val configurationCodec: Codec<ConfiguredResearchTrigger<E, C, ResearchTrigger<E, C>>> = codec
        .fieldOf("config")
        .xmap({ configure(it) }, { it.config })
        .codec()

    private fun configure(config: C): ConfiguredResearchTrigger<E, C, ResearchTrigger<E, C>> {
        return ConfiguredResearchTrigger(this, config)
    }

    abstract fun getEventType(config: C): KClass<E>

    abstract fun getAffectedPlayer(event: E, config: C): PlayerEntity?

    abstract fun shouldUnlock(player: PlayerEntity, event: E, config: C): Boolean
}