package com.davidm1a2.afraidofthedark.common.dimension.voidChest

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.generateSchematic
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Blocks
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

    override fun decorate(region: WorldGenRegion) {
        // Do nothing, there's nothing to decorate
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
        createBarrierBox(chunk)
        chunk.status = ChunkStatus.BASE
    }

    private fun createBarrierBox(chunk: IChunk) {
        val chunkPos = chunk.pos

        val currentIsland = ((chunkPos.x - 1) * 16) / Constants.DISTANCE_BETWEEN_ISLANDS
        // The X position in blockpos not chunkpos
        val xPos = currentIsland * Constants.DISTANCE_BETWEEN_ISLANDS
        // The barrier block state
        val barrier = Blocks.BARRIER.defaultState
        // If we've hit one of the critical chunks trigger a void chest generation
        if (chunkPos.z == 0 || chunkPos.z == 1 || chunkPos.z == 2 || chunkPos.z == 3) {
            for (i in 0..48) {
                for (j in 0..48) {
                    // Create the floor
                    setBlockIfInChunk(chunk, BlockPos(xPos + i, 100, j), barrier)
                    // Create the roof
                    setBlockIfInChunk(chunk, BlockPos(xPos + i, 100 + 48, j), barrier)
                    // Create the left wall
                    setBlockIfInChunk(chunk, BlockPos(xPos + 0, 100 + i, j), barrier)
                    // Create the right wall
                    setBlockIfInChunk(chunk, BlockPos(xPos + 48, 100 + i, j), barrier)
                    // Create the front wall
                    setBlockIfInChunk(chunk, BlockPos(xPos + i, 100 + j, 0), barrier)
                    // Create the back wall
                    setBlockIfInChunk(chunk, BlockPos(xPos + i, 100 + j, 48), barrier)
                }
            }

            // Generate the portal
            chunk.generateSchematic(ModSchematics.VOID_CHEST_PORTAL, BlockPos(xPos + 20, 100, -2), chunkPos)
        }
    }

    private fun setBlockIfInChunk(chunk: IChunk, blockPos: BlockPos, state: IBlockState) {
        if (blockPos.x >= chunk.pos.xStart && blockPos.x <= chunk.pos.xEnd && blockPos.z >= chunk.pos.zStart && blockPos.z <= chunk.pos.zEnd) {
            chunk.setBlockState(blockPos, state, false)
        }
    }

    override fun spawnMobs(p0: WorldGenRegion) {
        // Ignore
    }

    override fun spawnMobs(p0: World, p1: Boolean, p2: Boolean): Int {
        // Ignore
        return 0
    }
}