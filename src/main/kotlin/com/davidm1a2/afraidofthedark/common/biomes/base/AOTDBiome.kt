package com.davidm1a2.afraidofthedark.common.biomes.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.util.ResourceLocation
import net.minecraft.world.biome.Biome

/**
 * Base class for all AOTD biomes
 *
 * @param name The name of the biome
 * @param biomeBuilder The builder for the biome
 */
abstract class AOTDBiome(name: String, biomeBuilder: Builder) : Biome(biomeBuilder) {
    init {
        registryName = ResourceLocation(Constants.MOD_ID, name)
    }

    /**
     * Adds all the default entity spawns to the biome
     */
    fun addDefaultEntitySpawns() {
        addSpawn(EntityClassification.CREATURE, SpawnListEntry(EntityType.SHEEP, 12, 4, 4))
        addSpawn(EntityClassification.CREATURE, SpawnListEntry(EntityType.PIG, 10, 4, 4))
        addSpawn(EntityClassification.CREATURE, SpawnListEntry(EntityType.CHICKEN, 10, 4, 4))
        addSpawn(EntityClassification.CREATURE, SpawnListEntry(EntityType.COW, 8, 4, 4))
        addSpawn(EntityClassification.CREATURE, SpawnListEntry(EntityType.WOLF, 5, 4, 4))
        addSpawn(EntityClassification.AMBIENT, SpawnListEntry(EntityType.BAT, 10, 8, 8))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.SPIDER, 100, 4, 4))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.ZOMBIE, 95, 4, 4))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.SKELETON, 100, 4, 4))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.CREEPER, 100, 4, 4))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.SLIME, 100, 4, 4))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.ENDERMAN, 10, 1, 4))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.WITCH, 5, 1, 1))
    }
}