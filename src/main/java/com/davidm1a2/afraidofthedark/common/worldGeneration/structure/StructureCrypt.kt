package com.davidm1a2.afraidofthedark.common.worldGeneration.structure

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.world.IHeightmap
import com.davidm1a2.afraidofthedark.common.capabilities.world.OverworldHeightmap
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator.generateSchematic
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.iterator.InteriorChunkIterator
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor
import net.minecraft.init.Biomes
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeProvider
import kotlin.math.max
import kotlin.math.min

/**
 * Crypt structure class
 *
 * @constructor just initializes the name
 */
class StructureCrypt : AOTDStructure("crypt")
{
    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos      The position that the structure would begin at
     * @param heightmap     The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return true if the structure fits at the position, false otherwise
     */
    override fun computeChanceToGenerateAt(blockPos: BlockPos, heightmap: IHeightmap, biomeProvider: BiomeProvider): Double
    {
        return processChunks(object : IChunkProcessor<Double>
        {
            // Compute the minimum and maximum height over all the chunks that the crypt will cross over
            var minHeight = Int.MAX_VALUE
            var maxHeight = Int.MIN_VALUE

            // Counters for the number of erie forest chunks
            var numErieForestChunks = 0
            var numOtherChunks = 0
            override fun processChunk(chunkPos: ChunkPos): Boolean
            {
                val biomes = approximateBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z)
                // Filter incompatible biomes
                when
                {
                    biomes.any { INCOMPATIBLE_BIOMES.contains(it) } ->
                    {
                        return false
                    }
                    biomes.contains(ModBiomes.ERIE_FOREST) ->
                    {
                        numErieForestChunks++
                    }
                    else ->
                    {
                        numOtherChunks++
                    }
                }
                // Compute min and max height
                minHeight = min(minHeight, heightmap.getLowestHeight(chunkPos))
                maxHeight = max(maxHeight, heightmap.getHighestHeight(chunkPos))
                return true
            }

            override fun getResult(): Double
            {
                // If there's more than 5 blocks between the top and bottom block it's an invalid place for a crypt because it's not 'flat' enough
                if (maxHeight - minHeight > 5)
                {
                    return getDefaultResult()
                }

                // Compute how many chunks are erie forest and how many are other biomes
                val percentErie = numErieForestChunks.toDouble() / (numErieForestChunks + numOtherChunks)
                val percentOther = 1.0 - percentErie

                // 0.6% chance to spawn in other biomes, 2% chance to spawn in erie forests
                return (percentErie * 0.02 + percentOther * 0.006) * AfraidOfTheDark.INSTANCE.configurationHandler.cryptMultiplier
            }

            override fun getDefaultResult(): Double
            {
                return 0.0
            }
        }, InteriorChunkIterator(this, blockPos))
    }

    /**
     * Generates the structure at a position with an optional argument of chunk position
     *
     * @param world    The world to generate the structure in
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data     Data containing structure position
     */
    override fun generate(world: World, chunkPos: ChunkPos, data: NBTTagCompound)
    {
        // Get the position of the structure from the data compound
        val blockPos = getPosition(data)

        // This structure is simple, it is just the crypt schematic
        generateSchematic(ModSchematics.CRYPT, world, blockPos, chunkPos, ModLootTables.CRYPT)
    }

    /**
     * Called to generate a random permutation of the structure. Set the structure's position
     *
     * @param world         The world to generate the structure's data for
     * @param blockPos      The position's x and z coordinates to generate the structure at
     * @param biomeProvider ignored
     * @return The NBTTagCompound containing any data needed for generation. Sent in Structure::generate
     */
    override fun generateStructureData(world: World, blockPos: BlockPos, biomeProvider: BiomeProvider): NBTTagCompound
    {
        @Suppress("NAME_SHADOWING")
        var blockPos = blockPos
        val compound = NBTTagCompound()

        // Compute the center block of the schematic
        val centerBlock = blockPos.add(getXWidth() / 2, 0, getZLength() / 2)
        // Convert that block to the chunk it is in
        val centerChunk = ChunkPos(centerBlock)
        // Compute the ground height at the center
        val groundHeight = OverworldHeightmap.get(world).getLowestHeight(centerChunk)

        // Set the schematic height to be underground + 3 blocks+, ensure it isn't below bedrock
        val y = MathHelper.clamp(groundHeight - ModSchematics.CRYPT.getHeight() + 3, 5, Int.MAX_VALUE)
        blockPos = BlockPos(blockPos.x, y, blockPos.z)

        // Update the NBT
        compound.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos))

        return compound
    }

    /**
     * @return The width of the structure in blocks
     */
    override fun getXWidth(): Int
    {
        // For this structure the width is just the width of the crypt
        return ModSchematics.CRYPT.getWidth().toInt()
    }

    /**
     * @return The length of the structure in blocks
     */
    override fun getZLength(): Int
    {
        // For this structure the length is just the length of the crypt
        return ModSchematics.CRYPT.getLength().toInt()
    }

    companion object
    {
        // A set of incompatible biomes
        private val INCOMPATIBLE_BIOMES = setOf(
            Biomes.OCEAN,
            Biomes.DEEP_OCEAN,
            Biomes.FROZEN_OCEAN,
            Biomes.BEACH,
            Biomes.FROZEN_RIVER,
            Biomes.RIVER,
            Biomes.SKY,
            Biomes.VOID,
            Biomes.STONE_BEACH
        )
    }
}