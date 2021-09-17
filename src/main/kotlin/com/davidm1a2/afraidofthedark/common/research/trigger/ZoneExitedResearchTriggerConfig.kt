package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Function

class ZoneExitedResearchTriggerConfig(lazyTileEntityType: Lazy<TileEntityType<*>>) : ResearchTriggerConfig {
    val tileEntityType: TileEntityType<*> by lazyTileEntityType

    companion object {
        val CODEC: Codec<ZoneExitedResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ForgeRegistries.TILE_ENTITIES.codec().lazy().fieldOf("tile_entity").forGetter { config -> lazyOf(config.tileEntityType) }
            ).apply(it, it.stable(Function { tileEntityType ->
                ZoneExitedResearchTriggerConfig(tileEntityType)
            }))
        }
    }
}