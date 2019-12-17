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
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.LowestHeightChunkProcessor
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeProvider
import kotlin.math.max
import kotlin.math.min

/**
 * Witch hut structure class
 *
 * @constructor just sets the registry name
 */
class StructureWitchHut : AOTDStructure("witch_hut")
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
            // Compute the minimum and maximum height over all the chunks that the witch hut will cross over
            var minHeight = Int.MAX_VALUE
            var maxHeight = Int.MIN_VALUE

            override fun processChunk(chunkPos: ChunkPos): Boolean
            {
                val biomes = approximateBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z)

                // Witch huts can only spawn in erie forests
                if (!biomes.contains(ModBiomes.ERIE_FOREST) || biomes.size > 1)
                {
                    return false
                }

                // Compute min and max height
                minHeight = min(minHeight, heightmap.getLowestHeight(chunkPos))
                maxHeight = max(maxHeight, heightmap.getHighestHeight(chunkPos))
                return true
            }

            override fun getResult(): Double
            {
                // If there's more than 3 blocks between the top and bottom block it's an invalid place for a witch hut because it's not 'flat' enough
                return if (maxHeight - minHeight > 3)
                {
                    getDefaultResult()
                }
                // 4% chance to generate in any chunks this fits in
                else
                {
                    0.04 * AfraidOfTheDark.INSTANCE.configurationHandler.witchHutMultiplier
                }
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
     * @param data     NBT data containing the structure's position
     */
    override fun generate(world: World, chunkPos: ChunkPos, data: NBTTagCompound)
    {
        // Grab the block pos from the NBT data
        val blockPos = getPosition(data)
        // This structure is simple, it is just the witch hut schematic
        generateSchematic(ModSchematics.WITCH_HUT, world, blockPos, chunkPos, ModLootTables.WITCH_HUT)
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

        // Find the lowest y value containing a block
        val groundLevel = processChunks(LowestHeightChunkProcessor(OverworldHeightmap.get(world)), InteriorChunkIterator(this, blockPos))
        // Set the schematic at the lowest point in the chunk
        blockPos = BlockPos(blockPos.x, groundLevel - 1, blockPos.z)
        // Update the NBT
        compound.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos))

        return compound
    }

    /**
     * @return The width of the structure in blocks
     */
    override fun getXWidth(): Int
    {
        // For this structure the width is just the width of the witch hut
        return ModSchematics.WITCH_HUT.getWidth().toInt()
    }

    /**
     * @return The length of the structure in blocks
     */
    override fun getZLength(): Int
    {
        // For this structure the length is just the length of the witch hut
        return ModSchematics.WITCH_HUT.getLength().toInt()
    }
}