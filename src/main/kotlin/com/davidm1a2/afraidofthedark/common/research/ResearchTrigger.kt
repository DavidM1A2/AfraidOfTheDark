package com.davidm1a2.afraidofthedark.common.research

import net.minecraftforge.registries.ForgeRegistryEntry

abstract class ResearchTrigger<C : ResearchTriggerConfig> : ForgeRegistryEntry<ResearchTrigger<*>>() {
    fun configure(config: C): ConfiguredResearchTrigger<ResearchTrigger<C>, C> {
        return ConfiguredResearchTrigger(this, config)
    }
}