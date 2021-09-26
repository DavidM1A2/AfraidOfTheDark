package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.datafixers.util.Function3
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.registries.ForgeRegistries
import java.util.*

class ItemCraftedResearchTriggerConfig(
    val item: Item,
    val count: Int?,
    val data: CompoundNBT?
) : ResearchTriggerConfig {
    companion object {
        val CODEC: Codec<ItemCraftedResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ForgeRegistries.ITEMS.codec()
                    .fieldOf("item")
                    .forGetter(ItemCraftedResearchTriggerConfig::item),
                Codec.INT.optionalFieldOf("count").forGetter { config -> Optional.ofNullable(config.count) },
                CompoundNBT.CODEC.optionalFieldOf("data").forGetter { config -> Optional.ofNullable(config.data) }
            ).apply(it, it.stable(Function3 { item, count, data ->
                ItemCraftedResearchTriggerConfig(item, count.orElse(null), data.orElse(null))
            }))
        }
    }
}