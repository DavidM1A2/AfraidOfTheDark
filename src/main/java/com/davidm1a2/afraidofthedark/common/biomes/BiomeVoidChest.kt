package com.davidm1a2.afraidofthedark.common.biomes

import com.davidm1a2.afraidofthedark.common.biomes.base.AOTDBiome
import net.minecraft.entity.EnumCreatureType
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder

/**
 * Void chest biome is used in the void chest dimension.
 *
 * @constructor initializes the biome's fields
 */
class BiomeVoidChest : AOTDBiome(
    "void_chest",
    BiomeBuilder()
        .waterColor(0x537B09)
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