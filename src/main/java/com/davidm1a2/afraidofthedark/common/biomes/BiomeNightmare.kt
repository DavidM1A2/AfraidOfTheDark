package com.davidm1a2.afraidofthedark.common.biomes

import com.davidm1a2.afraidofthedark.common.biomes.base.AOTDBiome
import net.minecraft.entity.EnumCreatureType
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder

/**
 * Biome to be used by the entire nightmare dimension. It does nothing but set colors
 *
 * @constructor initializes the biome's fields
 */
class BiomeNightmare : AOTDBiome(
    "nightmare",
    BiomeBuilder()
        .waterColor(0xFF0000)
        .category(Category.NONE)
        .precipitation(RainType.NONE)
        .surfaceBuilder(CompositeSurfaceBuilder(NOOP_SURFACE_BUILDER, STONE_STONE_GRAVEL_SURFACE))
) {
    /**
     * No spawns allowed
     *
     * @param creature The creature to spawn
     * @param entry The entry to spawn
     */
    override fun addSpawn(creature: EnumCreatureType, entry: SpawnListEntry) {
    }
}