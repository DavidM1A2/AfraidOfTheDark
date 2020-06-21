package com.davidm1a2.afraidofthedark.common.dimension.voidChest

import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.constants.ModServerConfiguration
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Blocks
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
 * Chunk generator for the void chest dimension, generates cubes of barrier blocks and portal
 *
 * @param world The world that the chunk generator is for
 * @param biomeProvider The biome provider to use
 * @property noiseGen The noise generator
 */
class VoidChestChunkGenerator(world: IWorld, biomeProvider: BiomeProvider) : AbstractChunkGenerator<ChunkGenSettings>(world, biomeProvider) {
    private val noiseGen = NoiseGeneratorPerlin(SharedSeedRandom(seed), 4)

    override fun carve(worldGenRegion: WorldGenRegion, stage: GenerationStage.Carving) {
        // Do nothing, there's nothing to carve out of void
    }

    override fun decorate(region: WorldGenRegion) {
        val x = region.mainChunkX
        val z = region.mainChunkZ

        // The blocks between islands
        val blocksBetweenIslands = ModServerConfiguration.blocksBetweenIslands
        // The X position in blockpos not chunkpos
        val xPos = x * 16
        // The barrier block state
        val barrierDefaultState = Blocks.BARRIER.defaultState
        // If we've hit one of the critical chunks trigger a void chest generation
        if (xPos % blocksBetweenIslands == 0 && z == 0) {
            for (i in 0..48) {
                for (j in 0..48) {
                    // Create the floor
                    world.setBlockState(BlockPos(xPos + i, 100, z + j), barrierDefaultState, 3)
                    // Create the roof
                    world.setBlockState(BlockPos(xPos + i, 100 + 48, z + j), barrierDefaultState, 3)
                    // Create the left wall
                    world.setBlockState(BlockPos(xPos + 0, 100 + i, z + j), barrierDefaultState, 3)
                    // Create the right wall
                    world.setBlockState(BlockPos(xPos + 48, 100 + i, z + j), barrierDefaultState, 3)
                    // Create the front wall
                    world.setBlockState(BlockPos(xPos + i, 100 + j, z + 0), barrierDefaultState, 3)
                    // Create the back wall
                    world.setBlockState(BlockPos(xPos + i, 100 + j, z + 48), barrierDefaultState, 3)
                }
            }

            // Generate the portal
            SchematicGenerator.generateSchematic(ModSchematics.VOID_CHEST_PORTAL, world as World, BlockPos(xPos + 20, 100, -2))
        }
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
        chunk.biomes.fill(ModBiomes.VOID_CHEST)
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