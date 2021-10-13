package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.research.trigger.base.ConfiguredResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraftforge.eventbus.api.Event
import java.util.function.BiFunction

class MultiplicityResearchTriggerConfig(
    val baseTrigger: ConfiguredResearchTrigger<Event, *, *>,
    val times: Int
) : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<MultiplicityResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ConfiguredResearchTrigger.CODEC.fieldOf("base_trigger").forGetter(MultiplicityResearchTriggerConfig::baseTrigger),
                Codec.INT.fieldOf("times").forGetter(MultiplicityResearchTriggerConfig::times)
            ).apply(it, it.stable(BiFunction { trigger, timesRequired ->
                @Suppress("UNCHECKED_CAST")
                MultiplicityResearchTriggerConfig(trigger as ConfiguredResearchTrigger<Event, *, *>, timesRequired)
            }))
        }
    }
}