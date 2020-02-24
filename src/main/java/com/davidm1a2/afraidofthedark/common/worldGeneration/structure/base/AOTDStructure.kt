package com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base

import com.davidm1a2.afraidofthedark.common.capabilities.world.OverworldHeightmap
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.iterator.IChunkIterator
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.iterator.InteriorChunkIterator
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.LowestHeightChunkProcessor
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeProvider


/**
 * Base class for all AOTD structures
 */
abstract class AOTDStructure(baseName: String) : Structure() {
    init {
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
    }

    /**
     * Called to process/aggregate data about chunks of the structure if placed at a given position
     *
     * @param chunkProcessor The processor to process the chunk with
     * @param chunkIterator  The iterator used to go over the chunks
     * @param <T>            The type of result to return after processing
     * @return The result of the chunk processor type
     */
    protected open fun <T> processChunks(chunkProcessor: IChunkProcessor<T>, chunkIterator: IChunkIterator): T {
        for (chunkPos in chunkIterator.getChunks()) {
            // If process chunk returns false then return the default result
            if (!chunkProcessor.processChunk(chunkPos)) {
                return chunkProcessor.getDefaultResult()!!
            }
        }

        // All chunks were processed so return the actual result
        return chunkProcessor.getResult()
    }

    /**
     * Approximates the biomes in a chunk by testing the 4 corners and the center
     *
     * @param biomeProvider The biome provider
     * @param chunkX        The chunk's X coordinate
     * @param chunkZ        The chunk's Z coordinate
     * @return A set of biomes found at the 4 corners and center of the chunk
     */
    protected open fun approximateBiomesInChunk(biomeProvider: BiomeProvider, chunkX: Int, chunkZ: Int): Set<Biome> {
        val temp = arrayOfNulls<Biome>(1)
        return setOf(
            biomeProvider.getBiomes(temp, chunkX * 16 + 0, chunkZ * 16 + 0, 1, 1)[0],
            biomeProvider.getBiomes(temp, chunkX * 16 + 8, chunkZ * 16 + 8, 1, 1)[0],
            biomeProvider.getBiomes(temp, chunkX * 16 + 15, chunkZ * 16 + 0, 1, 1)[0],
            biomeProvider.getBiomes(temp, chunkX * 16 + 0, chunkZ * 16 + 15, 1, 1)[0],
            biomeProvider.getBiomes(temp, chunkX * 16 + 15, chunkZ * 16 + 15, 1, 1)[0]
        )
    }

    /**
     * Called to generate a random permutation of the structure. Set the structure's position
     *
     * @param world         The world to generate the structure's data for
     * @param blockPos      The position's x and z coordinates to generate the structure at
     * @param biomeProvider ignored
     * @return The NBTTagCompound containing any data needed for generation. Sent in Structure::generate
     */
    override fun generateStructureData(world: World, blockPos: BlockPos, biomeProvider: BiomeProvider): NBTTagCompound {
        @Suppress("NAME_SHADOWING")
        var blockPos = blockPos
        val compound = NBTTagCompound()
        // By default set the position to be on ground level
        val yPos = processChunks(
            LowestHeightChunkProcessor(OverworldHeightmap.get(world)),
            InteriorChunkIterator(this, blockPos)
        )

        // Update the y coordinate
        blockPos = BlockPos(blockPos.x, yPos, blockPos.z)

        // Set the pos tag
        compound.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos))

        return compound
    }
}
