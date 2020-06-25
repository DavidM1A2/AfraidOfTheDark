package com.davidm1a2.afraidofthedark.common.dimension.nightmare

import net.minecraft.entity.EnumCreatureType
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.chunk.ChunkStatus
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.*

/**
 * Chunk generator for the nightmare dimension
 *
 * @param world The nightmare world instance
 * @param biomeProvider The biome provider to use
 * @property noiseGen The noise generator
 */
class NightmareChunkGenerator(world: IWorld, biomeProvider: BiomeProvider) : AbstractChunkGenerator<ChunkGenSettings>(world, biomeProvider) {
    private val noiseGen = NoiseGeneratorPerlin(SharedSeedRandom(seed), 4)

    override fun carve(worldGenRegion: WorldGenRegion, stage: GenerationStage.Carving) {
        // Do nothing, there's nothing to carve out of void
    }

    override fun decorate(worldGenRegion: WorldGenRegion) {
        /*
        val x = worldGenRegion.mainChunkX
        val z = worldGenRegion.mainChunkZ
        // Grab some constants
        val islandWidth = ModStructures.NIGHTMARE_ISLAND.getXWidth()
        val islandLength = ModStructures.NIGHTMARE_ISLAND.getZLength()
        val blocksBetweenIslands = ModServerConfiguration.blocksBetweenIslands
        // Compute the z position
        val zPos = z * 16
        // Ensure z is between [0, len(island)+15]
        if (zPos >= 0 && zPos <= islandLength + 15) {
            // Compute the x position
            val xPos = x * 16
            // Grab the island index
            val islandIndex = xPos / blocksBetweenIslands
            // Compute the relative x position of the island between 0 and blocksBetweenIslands
            val relativeXPos = xPos % blocksBetweenIslands
            // Ensure the relative x pos will have blocks inside of it
            if (relativeXPos >= 0 && relativeXPos <= islandWidth + 15) {
                // Compute the position the island would generate at
                val islandPos = BlockPos(islandIndex * blocksBetweenIslands, 0, 0)
                // Compute data for the structure
                val data = ModStructures.NIGHTMARE_ISLAND.generateStructureData(world as World, islandPos, world.chunkProvider.chunkGenerator.biomeProvider)
                val chunkPos = ChunkPos(x, z)
                // Generate the chunk
                ModStructures.NIGHTMARE_ISLAND.generate(world, chunkPos, data)
                // Fix the lighting
                world.relightChunk(chunkPos)
            }
        }
        */
    }

    override fun getPossibleCreatures(creatureType: EnumCreatureType, pos: BlockPos): List<SpawnListEntry> {
        return emptyList()
    }

    override fun getGroundHeight(): Int {
        // Spawn players at sky height, this doesnt matter since we teleport the player on spawn/death
        return 256
    }

    override fun generateNoiseRegion(x: Int, z: Int): DoubleArray {
        // Copied from ChunkGeneratorEnd
        return noiseGen.generateRegion((x shl 4).toDouble(), (z shl 4).toDouble(), 16, 16, 0.0625, 0.0625, 1.0)
    }

    override fun getSettings(): ChunkGenSettings {
        return ChunkGenSettings()
    }

    override fun makeBase(chunk: IChunk) {
        val x = chunk.pos.x
        val z = chunk.pos.z
        chunk.biomes = biomeProvider.getBiomes(x * 16, z * 16, 16, 16)
        chunk.createHeightMap(Heightmap.Type.WORLD_SURFACE_WG, Heightmap.Type.OCEAN_FLOOR_WG)
        chunk.status = ChunkStatus.BASE
    }

    override fun spawnMobs(p0: WorldGenRegion) {
        // Ignore
    }

    override fun spawnMobs(p0: World, p1: Boolean, p2: Boolean): Int {
        // Ignore
        return 0
    }
}