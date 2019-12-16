package com.davidm1a2.afraidofthedark.common.worldGeneration.structure

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.world.IHeightmap
import com.davidm1a2.afraidofthedark.common.capabilities.world.OverworldHeightmap
import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator.generateSchematic
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.iterator.InteriorChunkIterator
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.LowestHeightChunkProcessor
import net.minecraft.init.Biomes
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeProvider
import kotlin.math.max
import kotlin.math.min

/**
 * Void chest structure class
 *
 * @constructor just sets the registry name
 */
class StructureVoidChest : AOTDStructure("void_chest")
{
    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos      The position that the structure would begin at
     * @param heightmap     The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return A value between 0 and 1 which is the chance between 0% and 100% that a structure could spawn at the given position
     */
    override fun computeChanceToGenerateAt(blockPos: BlockPos, heightmap: IHeightmap, biomeProvider: BiomeProvider): Double
    {
        return processChunks(object : IChunkProcessor<Double>
        {
            // Compute the minimum and maximum height over all the chunks that the void chest will cross over
            var minHeight = Int.MAX_VALUE
            var maxHeight = Int.MIN_VALUE

            override fun processChunk(chunkPos: ChunkPos): Boolean
            {
                val biomes = approximateBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z)
                // Void Chests only spawn in snowy biomes
                if (biomes.none { COMPATIBLE_BIOMES.contains(it) })
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
                // If there's more than 8 blocks between the top and bottom block it's an invalid place for a void chest because it's not 'flat' enough
                return if (maxHeight - minHeight > 8)
                {
                    getDefaultResult()
                }
                // 0.8% chance to generate in any chunks this fits in
                else
                {
                    0.008 * AfraidOfTheDark.INSTANCE.configurationHandler.voidChestMultiplier
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
     * @param data     NBT containing the void chest's position
     */
    override fun generate(world: World, chunkPos: ChunkPos, data: NBTTagCompound)
    {
        // Get the void chest's position from the NBT data
        val blockPos = getPosition(data)
        // This structure is simple, it is just the void chest schematic
        generateSchematic(ModSchematics.VOID_CHEST, world, blockPos, chunkPos, ModLootTables.VOID_CHEST)
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
        var blockPos = blockPos
        val compound = NBTTagCompound()

        // Find the lowest y value containing a block
        val groundLevel = processChunks(LowestHeightChunkProcessor(OverworldHeightmap.get(world)), InteriorChunkIterator(this, blockPos))
        // Set the schematic's position to the lowest point in the chunk
        blockPos = BlockPos(blockPos.x, groundLevel - 7, blockPos.z)
        // Update the NBT
        compound.setTag(NBT_POSITION, NBTUtil.createPosTag(blockPos))

        return compound
    }

    /**
     * @return The width of the structure in blocks
     */
    override fun getXWidth(): Int
    {
        return ModSchematics.VOID_CHEST.getWidth().toInt()
    }

    /**
     * @return The length of the structure in blocks
     */
    override fun getZLength(): Int
    {
        return ModSchematics.VOID_CHEST.getLength().toInt()
    }

    companion object
    {
        // A set of compatible biomes
        private val COMPATIBLE_BIOMES = setOf(
            Biomes.COLD_BEACH,
            Biomes.COLD_TAIGA,
            Biomes.COLD_TAIGA_HILLS,
            Biomes.MUTATED_TAIGA_COLD,
            Biomes.FROZEN_OCEAN,
            Biomes.FROZEN_RIVER,
            Biomes.ICE_PLAINS,
            Biomes.ICE_MOUNTAINS,
            Biomes.MUTATED_ICE_FLATS
        )
    }
}