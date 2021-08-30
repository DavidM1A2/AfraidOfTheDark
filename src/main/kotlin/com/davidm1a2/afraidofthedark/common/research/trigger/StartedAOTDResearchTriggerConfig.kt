package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec

class StartedAOTDResearchTriggerConfig : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<StartedAOTDResearchTriggerConfig> = Codec.unit(StartedAOTDResearchTriggerConfig())
    }
}