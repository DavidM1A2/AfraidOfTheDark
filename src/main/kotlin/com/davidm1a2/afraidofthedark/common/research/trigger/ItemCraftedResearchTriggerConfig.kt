package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.datafixers.util.Function3
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.registries.ForgeRegistries
import java.util.Optional

class ItemCraftedResearchTriggerConfig(
    lazyItem: Lazy<Item>,
    val count: Int?,
    val data: CompoundNBT?
) : ResearchTriggerConfig {
    val item: Item by lazyItem

    companion object {
        val CODEC: Codec<ItemCraftedResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ForgeRegistries.ITEMS.codec()
                    .lazy()
                    .fieldOf("item")
                    .forGetter { config -> lazyOf(config.item) },
                Codec.INT.optionalFieldOf("count").forGetter { config -> Optional.ofNullable(config.count) },
                CompoundNBT.CODEC.optionalFieldOf("data").forGetter { config -> Optional.ofNullable(config.data) }
            ).apply(it, it.stable(Function3 { item, count, data ->
                ItemCraftedResearchTriggerConfig(item, count.orElse(null), data.orElse(null))
            }))
        }
    }
}