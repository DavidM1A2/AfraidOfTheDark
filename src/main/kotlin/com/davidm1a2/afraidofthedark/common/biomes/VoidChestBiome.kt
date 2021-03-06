package com.davidm1a2.afraidofthedark.common.biomes

import com.davidm1a2.afraidofthedark.common.biomes.base.AOTDBiome
import net.minecraft.entity.EntityClassification
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder

/**
 * Void chest biome is used in the void chest dimension.
 *
 * @constructor initializes the biome's fields
 */
class VoidChestBiome : AOTDBiome(
    "void_chest",
    Builder()
        .surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG)
        .waterColor(0x5851C6)
        .waterFogColor(0x585186)
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