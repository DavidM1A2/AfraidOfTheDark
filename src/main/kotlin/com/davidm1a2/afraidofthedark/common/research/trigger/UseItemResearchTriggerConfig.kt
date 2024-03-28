package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.Item
import net.minecraftforge.registries.ForgeRegistries
import java.util.*
import java.util.function.BiFunction

class UseItemResearchTriggerConfig(
    lazyItem: Lazy<Item>,
    val data: CompoundTag?
) : ResearchTriggerConfig {
    val item: Item by lazyItem

    companion object {
        val CODEC: Codec<UseItemResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ForgeRegistries.ITEMS.codec()
                    .lazy()
                    .fieldOf("item")
                    .forGetter { config -> lazyOf(config.item) },
                CompoundTag.CODEC.optionalFieldOf("data").forGetter { config -> Optional.ofNullable(config.data) }
            ).apply(it, it.stable(BiFunction { item, data ->
                UseItemResearchTriggerConfig(item, data.orElse(null))
            }))
        }
    }
}