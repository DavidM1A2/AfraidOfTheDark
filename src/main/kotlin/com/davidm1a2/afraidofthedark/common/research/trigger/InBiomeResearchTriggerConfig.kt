package com.davidm1a2.afraidofthedark.common.research.trigger

import com.davidm1a2.afraidofthedark.common.registry.codec
import com.davidm1a2.afraidofthedark.common.registry.lazy
import com.davidm1a2.afraidofthedark.common.research.trigger.base.ResearchTriggerConfig
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.biome.Biome
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Function

class InBiomeResearchTriggerConfig(lazyBiome: Lazy<Biome>) : ResearchTriggerConfig {
    val biome: Biome by lazyBiome

    companion object {
        val CODEC: Codec<InBiomeResearchTriggerConfig> = RecordCodecBuilder.create {
            it.group(
                ForgeRegistries.BIOMES.codec()
                    .lazy()
                    .fieldOf("biome")
                    .forGetter { config -> lazyOf(config.biome) }
            ).apply(it, it.stable(Function { biome ->
                InBiomeResearchTriggerConfig(biome)
            }))
        }
    }
}