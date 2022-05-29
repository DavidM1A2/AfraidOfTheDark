package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.getOrNull
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.registry.toLazyOptional
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.function.BiFunction

class PlayerChangePowerSourceResearchTriggerConfig(oldPowerSource: Lazy<SpellPowerSource<*>?>, newPowerSource: Lazy<SpellPowerSource<*>?>) : ResearchTriggerConfig {
    val oldPowerSource: SpellPowerSource<*>? by oldPowerSource
    val newPowerSource: SpellPowerSource<*>? by newPowerSource

    companion object {
        val CODEC: Codec<PlayerChangePowerSourceResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ModRegistries.SPELL_POWER_SOURCES.codec()
                    .lazy()
                    .optionalFieldOf("oldPowerSource")
                    .forGetter { config -> config.oldPowerSource.toLazyOptional() },
                ModRegistries.SPELL_POWER_SOURCES.codec()
                    .lazy()
                    .optionalFieldOf("newPowerSource")
                    .forGetter { config -> config.newPowerSource.toLazyOptional() }
            ).apply(it, it.stable(BiFunction { oldPowerSource, newPowerSource ->
                PlayerChangePowerSourceResearchTriggerConfig(oldPowerSource.getOrNull(), newPowerSource.getOrNull())
            }))
        }
    }
}