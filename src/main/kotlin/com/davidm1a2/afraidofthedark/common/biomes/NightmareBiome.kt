package com.davidm1a2.afraidofthedark.common.biomes

import com.davidm1a2.afraidofthedark.common.biomes.base.AOTDBiome
import net.minecraft.entity.EntityClassification
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder

/**
 * Biome to be used by the entire nightmare dimension. It does nothing but set colors
 *
 * @constructor initializes the biome's fields
 */
class NightmareBiome : AOTDBiome(
    "nightmare",
    Builder()
        .surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG)
        .waterColor(0xFF0000)
        .waterFogColor(0x980000)
        .category(Category.NONE)
        .precipitation(RainType.NONE)
        .depth(0.1F)
        .scale(0.2F)
        .temperature(0.5F)
        .downfall(0.5F)
) {
    /**
     * No spawns allowed
     *
     * @param creature The creature to spawn
     * @param entry The entry to spawn
     */
    override fun addSpawn(creature: EntityClassification, entry: SpawnListEntry) {
    }
}