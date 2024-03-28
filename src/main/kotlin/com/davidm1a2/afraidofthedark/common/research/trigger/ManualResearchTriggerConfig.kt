package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.function.Function

/**
 * @param lazyResearch The research that this manual trigger will look for
 */
class ManualResearchTriggerConfig(lazyResearch: Lazy<Research>) : ResearchTriggerConfig {
    val research: Research by lazyResearch

    companion object {
        val CODEC: Codec<ManualResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ModRegistries.RESEARCH
                    .codec()
                    .lazy()
                    .fieldOf("research")
                    .forGetter { config -> lazyOf(config.research) }
            ).apply(it, it.stable(Function { research ->
                ManualResearchTriggerConfig(research)
            }))
        }
    }
}