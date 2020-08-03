package com.davidm1a2.afraidofthedark.common.dimension.voidChest

import net.minecraft.entity.EnumCreatureType
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.chunk.ChunkStatus
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.*

/**
 * Chunk generator for the void chest dimension, generates cubes of barrier blocks and portal
 *
 * @param world The world that the chunk generator is for
 * @param biomeProvider The biome provider to use
 * @property noiseGen The noise generator
 */
class VoidChestChunkGenerator(world: IWorld, biomeProvider: BiomeProvider) : AbstractChunkGenerator<ChunkGenSettings>(world, biomeProvider) {
    private val noiseGen = NoiseGeneratorPerlin(SharedSeedRandom(seed), 4)

    override fun buildSurface(chunkIn: IChunk, biomesIn: Array<Biome>, random: SharedSeedRandom, seaLevel: Int) {
        // Do nothing, void does not have a surface
    }

    override fun carve(worldGenRegion: WorldGenRegion, stage: GenerationStage.Carving) {
        // Do nothing, there's nothing to carve out of void
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