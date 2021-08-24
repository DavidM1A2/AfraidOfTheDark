package com.davidm1a2.afraidofthedark.common.research.trigger.base

import com.davidm1a2.afraidofthedark.common.constants.ModResearchTriggers
import com.mojang.serialization.Codec
import net.minecraft.util.ResourceLocation

class ConfiguredResearchTrigger<T : ResearchTrigger<*>, C : ResearchTriggerConfig>(
    val trigger: T,
    val config: C
) {
    companion object {
        val CODEC: Codec<ConfiguredResearchTrigger<*, *>> = ResourceLocation.CODEC.dispatch({ it.trigger.registryName }, {
            ModResearchTriggers.NAME_TO_TRIGGER[it]!!.configurationCodec
            // TODO: How can we pull from the registry before it is filled? :/ This is a hack but it works for now
            // ModRegistries.RESEARCH_TRIGGERS.getValue(it)!!.configuredCodec
        })
    }
}