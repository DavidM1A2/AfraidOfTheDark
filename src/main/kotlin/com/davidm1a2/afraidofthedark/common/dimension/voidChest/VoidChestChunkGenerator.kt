package com.davidm1a2.afraidofthedark.common.dimension.voidChest

import net.minecraft.entity.EntityClassification
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.biome.BiomeManager
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.WorldGenRegion
import net.minecraft.world.server.ServerWorld

/**
 * Chunk generator for the void chest dimension, generates cubes of barrier blocks and portal
 *
 * @param world The world that the chunk generator is for
 * @param biomeProvider The biome provider to use
 * @param settings The generation settings
 */
class VoidChestChunkGenerator(world: IWorld, biomeProvider: BiomeProvider, settings: GenerationSettings) :
    ChunkGenerator<GenerationSettings>(world, biomeProvider, settings) {
    // fun carve()
    override fun func_225550_a_(biomeManager: BiomeManager, chunk: IChunk, stage: GenerationStage.Carving) {
        // Do nothing, there's nothing to carve out of void
    }

    override fun getPossibleCreatures(creatureType: EntityClassification, pos: BlockPos): List<SpawnListEntry> {
        return emptyList()
    }

    override fun getGroundHeight(): Int {
        // Spawn players at sky height, this doesnt matter since we teleport the player on spawn/death
        return 256
    }

    override fun makeBase(world: IWorld, chunk: IChunk) {
        // chunk.createHeightMap()
    }

    override fun generateSurface(region: WorldGenRegion, chunk: IChunk) {
    }

    override fun func_222529_a(chunkX: Int, chunkZ: Int, heightmap: Heightmap.Type): Int {
        return 0
    }

    override fun spawnMobs(p0: WorldGenRegion) {
        // Ignore
    }

    override fun spawnMobs(p0: ServerWorld, p1: Boolean, p2: Boolean) {
        // Ignore
    }
}