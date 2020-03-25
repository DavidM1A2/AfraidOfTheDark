package com.davidm1a2.afraidofthedark.common.worldGeneration.structure

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.world.IHeightmap
import com.davidm1a2.afraidofthedark.common.capabilities.world.OverworldHeightmap
import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.iterator.InteriorChunkIterator
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor
import net.minecraft.init.Biomes
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagIntArray
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeProvider

/**
 * Desert oasis structure class
 *
 * @constructor just passes down the structure name
 */
class StructureDesertOasis : AOTDStructure("desert_oasis") {
    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos      The position that the structure would begin at
     * @param heightmap     The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return true if the structure fits at the position, false otherwise
     */
    override fun computeChanceToGenerateAt(
        blockPos: BlockPos,
        heightmap: IHeightmap,
        biomeProvider: BiomeProvider
    ): Double {
        return this.processChunks(object : IChunkProcessor<Double> {
            var numValidChunks = 0.0
            var numNonValidChunks = 0.0

            override fun processChunk(chunkPos: ChunkPos): Boolean {
                val biomes = getBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z)

                // If any biome is invalid this chunk is invalid, otherwise it's valid
                if (biomes.any { !VALID_BIOMES.contains(it) }) {
                    numNonValidChunks++
                } else {
                    numValidChunks++
                }

                return true
            }

            override fun getResult(): Double {
                val percentDesertTiles = numValidChunks / (numValidChunks + numNonValidChunks)
                // 70% valid tiles required, .5% chance to spawn
                return if (percentDesertTiles > 0.7) 0.005 * AfraidOfTheDark.INSTANCE.configurationHandler.desertOasisMultiplier else 0.0
            }

            override fun getDefaultResult(): Double {
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
    override fun generate(world: World, chunkPos: ChunkPos, data: NBTTagCompound) {
        // Get the position of the structure from the data compound
        val blockPos = getPosition(data)

        // Generate the oasis schematic first
        SchematicGenerator.generateSchematic(
            ModSchematics.DESERT_OASIS,
            world,
            blockPos,
            chunkPos,
            ModLootTables.DESERT_OASIS
        )

        // Generate picks for each of the 5 plot types (small, small90, medium, medium90, large)
        (data.getTag(NBT_SMALL_PLOT_INDICES) as NBTTagIntArray).intArray.forEachIndexed { index, pick ->
            SchematicGenerator.generateSchematic(
                ModSchematics.DESERT_OASIS_SMALL_PLOTS[pick],
                world,
                blockPos.add(PlotType.Small.baseOffsets[index]),
                chunkPos,
                ModLootTables.DESERT_OASIS
            )
        }

        (data.getTag(NBT_SMALL90_PLOT_INDICES) as NBTTagIntArray).intArray.forEachIndexed { index, pick ->
            SchematicGenerator.generateSchematic(
                ModSchematics.DESERT_OASIS_SMALL_PLOTS90[pick],
                world,
                blockPos.add(PlotType.Small90.baseOffsets[index]),
                chunkPos,
                ModLootTables.DESERT_OASIS
            )
        }

        (data.getTag(NBT_MEDIUM_PLOT_INDICES) as NBTTagIntArray).intArray.forEachIndexed { index, pick ->
            SchematicGenerator.generateSchematic(
                ModSchematics.DESERT_OASIS_MEDIUM_PLOTS[pick],
                world,
                blockPos.add(PlotType.Medium.baseOffsets[index]),
                chunkPos,
                ModLootTables.DESERT_OASIS
            )
        }

        (data.getTag(NBT_MEDIUM90_PLOT_INDICES) as NBTTagIntArray).intArray.forEachIndexed { index, pick ->
            SchematicGenerator.generateSchematic(
                ModSchematics.DESERT_OASIS_MEDIUM_PLOTS90[pick],
                world,
                blockPos.add(PlotType.Medium90.baseOffsets[index]),
                chunkPos,
                ModLootTables.DESERT_OASIS
            )
        }

        (data.getTag(NBT_LARGE_PLOT_INDICES) as NBTTagIntArray).intArray.forEachIndexed { index, pick ->
            SchematicGenerator.generateSchematic(
                ModSchematics.DESERT_OASIS_LARGE_PLOTS[pick],
                world,
                blockPos.add(PlotType.Large.baseOffsets[index]),
                chunkPos,
                ModLootTables.DESERT_OASIS
            )
        }
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
        val nbt = NBTTagCompound()

        val heightmap = OverworldHeightmap.get(world)
        val averageLowHeight =
            InteriorChunkIterator(this, blockPos).getChunks().map { heightmap.getLowestHeight(it) }.average().toInt()
        // Go down 18 blocks so the structure is spawned into the ground
        val yHeight = (averageLowHeight - 18).coerceIn(0..255)

        // Set the position to the base corner of the structure
        nbt.setTag(NBT_POSITION, NBTUtil.createPosTag(BlockPos(blockPos.x, yHeight, blockPos.z)))

        // Record picks for each of the 5 plot types (small, small90, medium, medium90, large)
        recordPlotPicks(
            nbt,
            NBT_SMALL_PLOT_INDICES,
            ModSchematics.DESERT_OASIS_SMALL_PLOTS.size,
            PlotType.Small.baseOffsets.size
        )
        recordPlotPicks(
            nbt,
            NBT_SMALL90_PLOT_INDICES,
            ModSchematics.DESERT_OASIS_SMALL_PLOTS90.size,
            PlotType.Small90.baseOffsets.size
        )
        recordPlotPicks(
            nbt,
            NBT_MEDIUM_PLOT_INDICES,
            ModSchematics.DESERT_OASIS_MEDIUM_PLOTS.size,
            PlotType.Medium.baseOffsets.size
        )
        recordPlotPicks(
            nbt,
            NBT_MEDIUM90_PLOT_INDICES,
            ModSchematics.DESERT_OASIS_MEDIUM_PLOTS90.size,
            PlotType.Medium90.baseOffsets.size
        )
        recordPlotPicks(
            nbt,
            NBT_LARGE_PLOT_INDICES,
            ModSchematics.DESERT_OASIS_LARGE_PLOTS.size,
            PlotType.Large.baseOffsets.size
        )

        return nbt
    }

    /**
     * Records what plots were picked at random
     *
     * @param nbt The NBT to write to
     * @param tagName The tag to write the results into
     * @param numTypes The number of possible plot types
     * @param numWanted The number of wanted plot types
     */
    private fun recordPlotPicks(nbt: NBTTagCompound, tagName: String, numTypes: Int, numWanted: Int) {
        require(numWanted <= numTypes * 2)
        val plotPicks = (0 until numTypes).shuffled().take(numWanted)
        val plotPicksNbt = NBTTagIntArray(plotPicks)
        nbt.setTag(tagName, plotPicksNbt)
    }

    /**
     * @return The width of the structure in blocks
     */
    override fun getXWidth(): Int {
        return ModSchematics.DESERT_OASIS.getWidth().toInt()
    }

    /**
     * @return The length of the structure in blocks
     */
    override fun getZLength(): Int {
        return ModSchematics.DESERT_OASIS.getLength().toInt()
    }

    companion object {
        private const val NBT_SMALL_PLOT_INDICES = "small_plot_indices"
        private const val NBT_SMALL90_PLOT_INDICES = "small90_plot_indices"
        private const val NBT_MEDIUM_PLOT_INDICES = "medium_plot_indices"
        private const val NBT_MEDIUM90_PLOT_INDICES = "medium90_plot_indices"
        private const val NBT_LARGE_PLOT_INDICES = "large_plot_indices"

        private val VALID_BIOMES = setOf(
            Biomes.DESERT,
            Biomes.MUTATED_DESERT,
            Biomes.RIVER
        )
    }

    private enum class PlotType(val baseOffsets: List<BlockPos>) {
        Small(
            listOf(
                BlockPos(154, 19, 141),
                BlockPos(93, 20, 161),
                BlockPos(132, 21, 78),
                BlockPos(43, 21, 99),
                BlockPos(35, 21, 136)
            )
        ),
        Small90(
            listOf(
                BlockPos(37, 21, 22),
                BlockPos(10, 21, 77),
                BlockPos(164, 21, 101)
            )
        ),
        Medium(
            listOf(
                BlockPos(122, 22, 129),
                BlockPos(155, 24, 74),
                BlockPos(84, 25, 119)
            )
        ),
        Medium90(
            listOf(
                BlockPos(113, 19, 61),
                BlockPos(45, 22, 42),
                BlockPos(76, 26, 57)
            )
        ),
        Large(
            listOf(
                BlockPos(132, 22, 31)
            )
        );
    }
}