package com.davidm1a2.afraidofthedark.common.research

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import com.mojang.datafixers.util.Function3
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

class ResearchSpellComponents(
    lazyDeliveryMethods: Lazy<List<SpellDeliveryMethod>>,
    lazyPowerSources: Lazy<List<SpellPowerSource<*>>>,
    lazyEffects: Lazy<List<SpellEffect>>
) {
    val deliveryMethods: List<SpellDeliveryMethod> by lazyDeliveryMethods
    val powerSources: List<SpellPowerSource<*>> by lazyPowerSources
    val effects: List<SpellEffect> by lazyEffects
    val components: List<SpellComponent<*>> by lazy {
        deliveryMethods + powerSources + effects
    }

    companion object {
        val EMPTY = ResearchSpellComponents(lazyOf(emptyList()), lazyOf(emptyList()), lazyOf(emptyList()))
        val CODEC: Codec<ResearchSpellComponents> = RecordCodecBuilder.create {
            it.group(
                ModRegistries.SPELL_DELIVERY_METHODS.codec()
                    .listOf()
                    .lazy()
                    .optionalFieldOf("delivery_methods", lazyOf(emptyList()))
                    .forGetter { components -> lazyOf(components.deliveryMethods) },
                ModRegistries.SPELL_POWER_SOURCES.codec()
                    .listOf()
                    .lazy()
                    .optionalFieldOf("power_sources", lazyOf(emptyList()))
                    .forGetter { components -> lazyOf(components.powerSources) },
                ModRegistries.SPELL_EFFECTS.codec()
                    .listOf()
                    .lazy()
                    .optionalFieldOf("effects", lazyOf(emptyList()))
                    .forGetter { components -> lazyOf(components.effects) }
            ).apply(it, it.stable(Function3 { deliveryMethods, powerSources, effects ->
                ResearchSpellComponents(deliveryMethods, powerSources, effects)
            }))
        }
    }
}