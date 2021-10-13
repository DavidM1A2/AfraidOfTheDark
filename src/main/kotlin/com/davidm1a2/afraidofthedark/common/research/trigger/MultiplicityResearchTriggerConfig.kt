package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.research.trigger.base.ConfiguredResearchTrigger
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.datafixers.util.Function3
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.ResourceLocation
import net.minecraftforge.eventbus.api.Event

class MultiplicityResearchTriggerConfig(
    val baseTrigger: ConfiguredResearchTrigger<Event, *, *>,
    val times: Int,
    val key: ResourceLocation
) : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<MultiplicityResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ConfiguredResearchTrigger.CODEC.fieldOf("base_trigger").forGetter(MultiplicityResearchTriggerConfig::baseTrigger),
                Codec.INT.fieldOf("times").forGetter(MultiplicityResearchTriggerConfig::times),
                ResourceLocation.CODEC.fieldOf("key").forGetter(MultiplicityResearchTriggerConfig::key)
            ).apply(it, it.stable(Function3 { trigger, timesRequired, key ->
                @Suppress("UNCHECKED_CAST")
                MultiplicityResearchTriggerConfig(trigger as ConfiguredResearchTrigger<Event, *, *>, timesRequired, key)
            }))
        }
    }
}