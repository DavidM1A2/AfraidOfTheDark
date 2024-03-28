package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import java.util.function.BiFunction

class PlayerHurtResearchTriggerConfig(attackingEntityTypeId: ResourceLocation, val mustSurvive: Boolean) : ResearchTriggerConfig {
    val attackingEntityType: EntityType<*> by lazy {
        EntityType.byString(attackingEntityTypeId.toString()).get()
    }

    companion object {
        val CODEC: Codec<PlayerHurtResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ResourceLocation.CODEC.fieldOf("attacking_entity_type").forGetter { config -> config.attackingEntityType.registryName },
                Codec.BOOL.optionalFieldOf("must_survive", false).forGetter(PlayerHurtResearchTriggerConfig::mustSurvive)
            ).apply(it, it.stable(BiFunction { entityType, mustSurvive ->
                PlayerHurtResearchTriggerConfig(entityType, mustSurvive)
            }))
        }
    }
}