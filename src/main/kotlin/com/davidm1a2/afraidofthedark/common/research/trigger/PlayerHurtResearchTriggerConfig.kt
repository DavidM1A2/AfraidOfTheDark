package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.function.Function

class PlayerHurtResearchTriggerConfig(val mustSurvive: Boolean) : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<PlayerHurtResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                Codec.BOOL.fieldOf("must_survive").forGetter(PlayerHurtResearchTriggerConfig::mustSurvive)
            ).apply(it, it.stable(Function { mustSurvive ->
                PlayerHurtResearchTriggerConfig(mustSurvive)
            }))
        }
    }
}