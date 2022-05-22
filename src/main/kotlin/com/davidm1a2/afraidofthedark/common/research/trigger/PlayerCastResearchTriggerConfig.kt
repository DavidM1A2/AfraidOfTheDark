package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.getOrNull
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.registry.toLazyOptional
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import com.mojang.datafixers.util.Function4
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.Optional

class PlayerCastResearchTriggerConfig(
    powerSourceLazy: Lazy<SpellPowerSource<*>?>,
    deliveryMethodLazy: Lazy<SpellDeliveryMethod?>,
    effectLazy: Lazy<SpellEffect?>,
    val minCost: Double?
) : ResearchTriggerConfig {
    val powerSource: SpellPowerSource<*>? by powerSourceLazy
    val deliveryMethod: SpellDeliveryMethod? by deliveryMethodLazy
    val effect: SpellEffect? by effectLazy

    companion object {
        val CODEC: Codec<PlayerCastResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ModRegistries.SPELL_POWER_SOURCES
                    .codec()
                    .lazy()
                    .optionalFieldOf("power_source")
                    .forGetter { config -> config.powerSource.toLazyOptional() },
                ModRegistries.SPELL_DELIVERY_METHODS
                    .codec()
                    .lazy()
                    .optionalFieldOf("delivery_method")
                    .forGetter { config -> config.deliveryMethod.toLazyOptional() },
                ModRegistries.SPELL_EFFECTS
                    .codec()
                    .lazy()
                    .optionalFieldOf("effect")
                    .forGetter { config -> config.effect.toLazyOptional() },
                Codec.DOUBLE.optionalFieldOf("minCost").forGetter { config -> Optional.ofNullable(config.minCost) }
            ).apply(it, it.stable(Function4 { powerSource, deliveryMethod, effect, minCost ->
                PlayerCastResearchTriggerConfig(powerSource.getOrNull(), deliveryMethod.getOrNull(), effect.getOrNull(), minCost.orElse(null))
            }))
        }
    }
}