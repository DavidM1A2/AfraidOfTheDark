package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.EntityType
import net.minecraft.util.ResourceLocation
import java.util.*
import com.mojang.datafixers.util.Function4

class PlayerCastResearchTriggerConfig(val powerSource: ResourceLocation?, val deliveryMethod: ResourceLocation?, val effect: ResourceLocation?, val minCost: Double?) : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<PlayerCastResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ResourceLocation.CODEC.optionalFieldOf("power_source").forGetter { config -> Optional.ofNullable(config.powerSource) },
                ResourceLocation.CODEC.optionalFieldOf("delivery_method").forGetter { config -> Optional.ofNullable(config.deliveryMethod) },
                ResourceLocation.CODEC.optionalFieldOf("effect").forGetter { config -> Optional.ofNullable(config.effect) },
                Codec.DOUBLE.optionalFieldOf("minCost").forGetter { config -> Optional.ofNullable(config.minCost) }
            ).apply(it, it.stable(Function4 { powerSource, deliveryMethod, effect, minCost ->
                PlayerCastResearchTriggerConfig(powerSource.orElse(null), deliveryMethod.orElse(null), effect.orElse(null), minCost.orElse(null))
            }))
        }
    }
}