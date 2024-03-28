package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Function

class ZoneEnteredResearchTriggerConfig(lazyTileEntityType: Lazy<BlockEntityType<*>>) : ResearchTriggerConfig {
    val tileEntityType: BlockEntityType<*> by lazyTileEntityType

    companion object {
        val CODEC: Codec<ZoneEnteredResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ForgeRegistries.BLOCK_ENTITIES.codec().lazy().fieldOf("tile_entity").forGetter { config -> lazyOf(config.tileEntityType) }
            ).apply(it, it.stable(Function { tileEntityType ->
                ZoneEnteredResearchTriggerConfig(tileEntityType)
            }))
        }
    }
}