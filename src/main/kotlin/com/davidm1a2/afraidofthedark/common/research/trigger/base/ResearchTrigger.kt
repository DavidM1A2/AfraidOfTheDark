package com.davidm1a2.afraidofthedark.common.research.trigger.base

import com.mojang.serialization.Codec
import net.minecraftforge.registries.ForgeRegistryEntry

abstract class ResearchTrigger<C : ResearchTriggerConfig>(codec: Codec<C>) : ForgeRegistryEntry<ResearchTrigger<*>>() {
    val configurationCodec: Codec<ConfiguredResearchTrigger<ResearchTrigger<C>, C>> = codec
        .fieldOf("config")
        .xmap({ configure(it) }, { it.config })
        .codec()

    fun configure(config: C): ConfiguredResearchTrigger<ResearchTrigger<C>, C> {
        return ConfiguredResearchTrigger(this, config)
    }
}