package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.registries.ForgeRegistries
import java.util.Optional
import java.util.function.BiFunction

class ItemSmeltedResearchTriggerConfig(
    lazyItem: Lazy<Item>,
    val data: CompoundNBT?
) : ResearchTriggerConfig {
    val item: Item by lazyItem

    companion object {
        val CODEC: Codec<ItemSmeltedResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ForgeRegistries.ITEMS.codec()
                    .lazy()
                    .fieldOf("item")
                    .forGetter { config -> lazyOf(config.item) },
                CompoundNBT.CODEC.optionalFieldOf("data").forGetter { config -> Optional.ofNullable(config.data) }
            ).apply(it, it.stable(BiFunction { item, data ->
                ItemSmeltedResearchTriggerConfig(item, data.orElse(null))
            }))
        }
    }
}