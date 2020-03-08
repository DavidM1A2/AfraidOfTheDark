package com.davidm1a2.afraidofthedark.common.biomes

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.EnumCreatureType
import net.minecraft.util.ResourceLocation
import net.minecraft.world.biome.Biome

/**
 * Biome to be used by the entire nightmare dimension. It does nothing but set colors
 *
 * @constructor initializes the biome's fields
 * @property spawnableCreatures A list of spawnable creatures, should always be empty
 */
class BiomeNightmare : Biome(
    BiomeProperties("Nightmare")
        .setWaterColor(0xFF0000)
        .setBaseHeight(0.125f)
        .setHeightVariation(0.05f)
        .setRainDisabled()
        .setRainfall(1.0f)
) {
    private val spawnableCreatures: MutableList<SpawnListEntry> = mutableListOf()

    init {
        // Set the biome's registry name to nightmare
        registryName = ResourceLocation(Constants.MOD_ID, "nightmare")
        // We will have no flowers or creatures in this biome
        flowers.clear()
        modSpawnableLists.values.forEach { it.clear() }
    }

    /**
     * Returns an empty list
     *
     * @param creatureType The creature type to test
     * @return An empty list
     */
    override fun getSpawnableList(creatureType: EnumCreatureType): List<SpawnListEntry> {
        spawnableCreatures.clear()
        return spawnableCreatures
    }

    /**
     * @return 0, nothing spawns here
     */
    override fun getSpawningChance(): Float {
        return 0.0f
    }
}