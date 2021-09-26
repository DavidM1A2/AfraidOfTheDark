package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.registries.ForgeRegistries
import java.util.Optional
import java.util.function.BiFunction

class UseItemResearchTriggerConfig(
    val item: Item,
    val data: CompoundNBT?
) : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<UseItemResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ForgeRegistries.ITEMS.codec().fieldOf("item").forGetter(UseItemResearchTriggerConfig::item),
                CompoundNBT.CODEC.optionalFieldOf("data").forGetter { config -> Optional.ofNullable(config.data) }
            ).apply(it, it.stable(BiFunction { item, data ->
                UseItemResearchTriggerConfig(item, data.orElse(null))
            }))
        }
    }
}