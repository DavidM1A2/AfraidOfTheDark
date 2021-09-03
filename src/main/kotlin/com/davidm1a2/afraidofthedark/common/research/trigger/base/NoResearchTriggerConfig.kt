package com.davidm1a2.afraidofthedark.common.research.trigger.base

import com.mojang.serialization.Codec

class NoResearchTriggerConfig : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<NoResearchTriggerConfig> = Codec.unit(NoResearchTriggerConfig())
    }
}