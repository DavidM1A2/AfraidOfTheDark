package com.davidm1a2.afraidofthedark.common.researchTriggers

import com.davidm1a2.afraidofthedark.common.constants.ModResearchTriggers
import com.mojang.serialization.Codec
import net.minecraft.util.ResourceLocation

class ConfiguredResearchTrigger<T : ResearchTrigger<*>, C : ResearchTriggerConfig>(
    val trigger: T,
    val config: C
) {
    companion object {
        val CODEC: Codec<ConfiguredResearchTrigger<*, *>> = ResourceLocation.CODEC.dispatch(
            {
                it.trigger.registryName
            },
            {
                // TODO: How can we pull from the registry before it is filled? :/
                ModResearchTriggers.PLAYER_HURT.configuredCodec
                // ModRegistries.RESEARCH_TRIGGERS.getValue(it)!!.configuredCodec
            }
        )
    }
}