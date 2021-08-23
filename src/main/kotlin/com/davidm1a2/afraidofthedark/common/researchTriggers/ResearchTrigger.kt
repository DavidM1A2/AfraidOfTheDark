package com.davidm1a2.afraidofthedark.common.researchTriggers

import com.mojang.serialization.Codec
import net.minecraftforge.registries.ForgeRegistryEntry

abstract class ResearchTrigger<C : ResearchTriggerConfig>(codec: Codec<C>) : ForgeRegistryEntry<ResearchTrigger<*>>() {
    val configuredCodec: Codec<ConfiguredResearchTrigger<ResearchTrigger<C>, C>> = codec
        .fieldOf("config")
        .xmap({ ConfiguredResearchTrigger(this, it) }, { it.config })
        .codec()

    fun configure(config: C): ConfiguredResearchTrigger<ResearchTrigger<C>, C> {
        return ConfiguredResearchTrigger(this, config)
    }
}