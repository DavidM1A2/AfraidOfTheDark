package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.EntityType
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.BiFunction

class KillEntityResearchTriggerConfig(
    lazyEntityType: Lazy<EntityType<*>>,
    val countRequired: Int
) : ResearchTriggerConfig {
    val entityType: EntityType<*> by lazyEntityType

    companion object {
        val CODEC: Codec<KillEntityResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ForgeRegistries.ENTITIES.codec()
                    .lazy()
                    .fieldOf("entity_type")
                    .forGetter { config -> lazyOf(config.entityType) },
                Codec.INT.optionalFieldOf("count_required", 1).forGetter(KillEntityResearchTriggerConfig::countRequired)
            ).apply(it, it.stable(BiFunction { entityType, numberRequired ->
                KillEntityResearchTriggerConfig(entityType, numberRequired)
            }))
        }
    }
}