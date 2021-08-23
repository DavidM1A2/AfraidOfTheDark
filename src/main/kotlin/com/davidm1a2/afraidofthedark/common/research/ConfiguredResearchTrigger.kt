package com.davidm1a2.afraidofthedark.common.research

import net.minecraftforge.registries.ForgeRegistryEntry

class ConfiguredResearchTrigger<T : ResearchTrigger<*>, C : ResearchTriggerConfig>(
    val trigger: T,
    val config: C
) : ForgeRegistryEntry<ConfiguredResearchTrigger<*, *>>()