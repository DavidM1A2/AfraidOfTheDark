package com.davidm1a2.afraidofthedark.common.worldGeneration.structure

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.world.IHeightmap
import com.davidm1a2.afraidofthedark.common.capabilities.world.OverworldHeightmap
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.Schematic
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator.generateSchematic
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.AOTDStructure
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.iterator.IChunkIterator
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.IChunkProcessor
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.processor.LowestHeightChunkProcessor
import net.minecraft.init.Biomes
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeProvider
import net.minecraftforge.common.util.Constants
import java.awt.Rectangle
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Dark forest structure class
 *
 * @constructor just sets the registry name
 * @property width The width of the dark forest dungeon
 * @property bedHouseWidth The bed house width
 * @property height The height of the dark forest dungeon
 * @property bedHouseLength The bed house length
 */
class StructureDarkForest : AOTDStructure("dark_forest") {
    private val width: Int
    private val bedHouseWidth: Int
    private val height: Int
    private val bedHouseLength: Int

    init {
        val widestTree = ModSchematics.DARK_FOREST_TREES.map { it.getWidth() }.maxWith(Comparator { tree1, tree2 ->
            tree1.compareTo(tree2)
        })!!.toInt()
        val longestTree = ModSchematics.DARK_FOREST_TREES.map { it.getLength() }.maxWith(Comparator { tree1, tree2 ->
            tree1.compareTo(tree2)
        })!!.toInt()

        bedHouseWidth = ModSchematics.BED_HOUSE.getWidth().toInt()
        bedHouseLength = ModSchematics.BED_HOUSE.getLength().toInt()

        // Width is width(BED_HOUSE) + 2*width(BIGGEST_TREE)
        width = bedHouseWidth + 2 * widestTree

        // Height is length(BED_HOUSE) + 2*length(BIGGEST_TREE)
        height = bedHouseLength + 2 * longestTree
    }

    /**
     * Tests if this structure is valid for the given position
     *
     * @param blockPos      The position that the structure would begin at
     * @param heightmap     The heightmap to use in deciding if the structure will fit at the position
     * @param biomeProvider The provider used to generate the world, use biomeProvider.getBiomes() to get what biomes exist at a position
     * @return A value between 0 and 1 which is the chance between 0% and 100% that a structure could spawn at the given position
     */
    override fun computeChanceToGenerateAt(
        blockPos: BlockPos,
        heightmap: IHeightmap,
        biomeProvider: BiomeProvider
    ): Double {
        // Compute the two chunk position corners of the house
        val houseCorner1BlockPos =
            blockPos.add(getXWidth() / 2 - bedHouseWidth / 2, 0, getZLength() / 2 - bedHouseLength / 2)
        val houseCorner2BlockPos = houseCorner1BlockPos.add(bedHouseWidth, 0, bedHouseLength)
        val houseCorner1ChunkPos = ChunkPos(houseCorner1BlockPos)
        val houseCorner2ChunkPos = ChunkPos(houseCorner2BlockPos)

        // Test all the chunks that the house will be in, ensure that all chunks that the house will be in are of the correct type
        val houseValid = processChunks(object : IChunkProcessor<Boolean> {
            // Compute the minimum and maximum height over all the chunks that the dark forest house will cross over
            var minHeight = Int.MAX_VALUE
            var maxHeight = Int.MIN_VALUE

            override fun processChunk(chunkPos: ChunkPos): Boolean {
                val biomes = approximateBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z)
                // Dark forest can only spawn in in plains or savannahs
                return if (COMPATIBLE_HOUSE_BIOMES.containsAll(biomes)) {
                    // Compute min and max height
                    minHeight = min(minHeight, heightmap.getLowestHeight(chunkPos))
                    maxHeight = max(maxHeight, heightmap.getHighestHeight(chunkPos))
                    true
                } else {
                    false
                }
            }

            override fun getResult(): Boolean {
                // If the height difference is less than 8 then it's OK to place the house here
                return maxHeight - minHeight < 8
            }

            override fun getDefaultResult(): Boolean {
                return false
            }
        },
            object : IChunkIterator {
                override fun getChunks(): List<ChunkPos> {
                    // Go over all chunks that the bed house would cover and check them
                    val houseChunks = mutableListOf<ChunkPos>()

                    for (chunkX in houseCorner1ChunkPos.x..houseCorner1ChunkPos.x) {
                        for (chunkZ in houseCorner2ChunkPos.z..houseCorner2ChunkPos.z) {
                            houseChunks.add(ChunkPos(chunkX, chunkZ))
                        }
                    }

                    return houseChunks
                }
            })

        // If the house isn't valid don't place a dark forest here
        return if (!houseValid) {
            0.0
        }
        // If the house is valid we're good to go, the chance to gen will be .2%
        else {
            0.002 * AfraidOfTheDark.INSTANCE.configurationHandler.darkForestMultiplier
        }
    }

    /**
     * Generates the structure at a position with an optional argument of chunk position
     *
     * @param world    The world to generate the structure in
     * @param chunkPos Optional chunk position of a chunk to generate in. If supplied all blocks generated must be in this chunk only!
     * @param data     Data containing info about structure placement like tree locations and prop positions
     */
    override fun generate(world: World, chunkPos: ChunkPos, data: NBTTagCompound) {
        // Create props first since they're least important and can be overridden
        val props = data.getTagList(NBT_PROPS, Constants.NBT.TAG_COMPOUND)
        for (i in 0 until props.tagCount()) {
            val schematicNBT = props.getCompoundTagAt(i)
            // Grab the schematic ID to generate
            val schematicId = schematicNBT.getInteger(NBT_SCHEMATIC_ID)
            // Grab the schematic to generate
            val schematic = ModSchematics.DARK_FOREST_PROPS[schematicId]
            // Grab the schematic position
            val schematicPos = NBTUtil.getPosFromTag(schematicNBT.getCompoundTag(NBT_POSITION))
            // Generate the schematic
            generateSchematic(schematic, world, schematicPos, chunkPos)
        }

        // Create trees second since they shouldn't override the house but can override props
        val trees = data.getTagList(NBT_TREES, Constants.NBT.TAG_COMPOUND)
        for (i in 0 until trees.tagCount()) {
            val schematicNBT = trees.getCompoundTagAt(i)
            // Grab the schematic ID to generate
            val schematicId = schematicNBT.getInteger(NBT_SCHEMATIC_ID)
            // Grab the schematic to generate
            val schematic = ModSchematics.DARK_FOREST_TREES[schematicId]
            // Grab the schematic position
            val schematicPos = NBTUtil.getPosFromTag(schematicNBT.getCompoundTag(NBT_POSITION))
            // Generate the schematic
            generateSchematic(schematic, world, schematicPos, chunkPos)
        }

        // Generate the bed house in the center last
        val housePos = NBTUtil.getPosFromTag(data.getCompoundTag(NBT_HOUSE_POSITION))
        generateSchematic(ModSchematics.BED_HOUSE, world, housePos, chunkPos, ModLootTables.DARK_FOREST)
    }

    /**
     * Store the prop and tree data into the NBT compound
     *
     * @param world         The world to generate the structure's data for
     * @param blockPos      The position's x and z coordinates to generate the structure at
     * @param biomeProvider The biome provider used to generate props on dry land only
     * @return The tree and prop positions used in generation and structure position
     */
    override fun generateStructureData(world: World, blockPos: BlockPos, biomeProvider: BiomeProvider): NBTTagCompound {
        /*
        Dark forest layout:
           ________________________________
          |                                |
          |            t gutter            | l
          |          ____________          | e
          |         |            |         | n
          | l gutter|   house    | r gutter| g
          |         |            |         | t
          |         |____________|         | h
          |                                |
          |            b gutter            |
          |________________________________|
                        width
         */

        // Compute the rectangles of each gutter -> x,y and width,height

        // One rectangle for each gutter
        val leftGutter = Rectangle(0, 0, (getXWidth() - bedHouseWidth) / 2, getZLength())
        val rightGutter = Rectangle(bedHouseWidth + leftGutter.width, 0, leftGutter.width, getZLength())
        val bottomGutter = Rectangle(0, 0, getXWidth(), (getZLength() - bedHouseLength) / 2)
        val topGutter =
            Rectangle(0, bedHouseLength + bottomGutter.height, getXWidth(), (getZLength() - bedHouseLength) / 2)

        // A list of gutters
        val gutters = listOf(leftGutter, rightGutter, bottomGutter, topGutter)
        val bedHouseSides = listOf(EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH)

        // Add random props, trees, and the bed house into the data
        val heightmap = OverworldHeightmap.get(world)
        val data = NBTTagCompound()

        addRandomProps(data, heightmap, blockPos, biomeProvider, gutters)
        addRandomTrees(data, heightmap, blockPos, gutters, bedHouseSides)
        setBedHousePosition(data, heightmap, blockPos)

        // Finally set the position of the structure to be at the same level as the house
        val houseY = NBTUtil.getPosFromTag(data.getCompoundTag(NBT_HOUSE_POSITION)).y
        data.setTag(NBT_POSITION, NBTUtil.createPosTag(BlockPos(blockPos.x, houseY, blockPos.z)))
        return data
    }

    /**
     * Sets the bed house NBT data
     *
     * @param data      The data to set forest be house position into
     * @param heightmap A heightmap to use to compute position
     * @param blockPos  The position the dark forest was placed at
     */
    private fun setBedHousePosition(data: NBTTagCompound, heightmap: IHeightmap, blockPos: BlockPos) {
        // Compute the house's y level so that it sits flat

        // Compute the two chunk position corners of the house
        val houseCorner1BlockPos =
            blockPos.add(getXWidth() / 2 - bedHouseWidth / 2, 0, getZLength() / 2 - bedHouseLength / 2)
        val houseCorner2BlockPos = houseCorner1BlockPos.add(bedHouseWidth, 0, bedHouseLength)

        // Find the lowest y value of the chunks covered by the bed house
        val houseCorner1ChunkPos = ChunkPos(houseCorner1BlockPos)
        val houseCorner2ChunkPos = ChunkPos(houseCorner2BlockPos)
        val minGroundHeight = processChunks(LowestHeightChunkProcessor(heightmap), object : IChunkIterator {
            override fun getChunks(): List<ChunkPos> {
                val houseChunks = mutableListOf<ChunkPos>()

                // Go over all chunks that the bed house is covering
                for (chunkX in houseCorner1ChunkPos.x..houseCorner2ChunkPos.x) {
                    for (chunkZ in houseCorner1ChunkPos.z..houseCorner2ChunkPos.z) {
                        houseChunks.add(ChunkPos(chunkX, chunkZ))
                    }
                }

                return houseChunks
            }
        })

        // Get the house position and set it in the data nbt
        val housePos = BlockPos(houseCorner1BlockPos.x, minGroundHeight, houseCorner1BlockPos.z)
        // Store the house's position into the NBT
        data.setTag(NBT_HOUSE_POSITION, NBTUtil.createPosTag(housePos))
    }

    /**
     * Adds between 25 - 100 random props in the world. These are small objects like stumps, logs, saplings, etc
     *
     * @param data          The data to add the props to
     * @param heightmap     The heightmap of the world to create props in
     * @param blockPos      The position to generate the structure at
     * @param biomeProvider The biome provider used to get information about the biomes a prop will be placed in
     * @param gutters       The gutters around the bed house to generate in
     */
    private fun addRandomProps(
        data: NBTTagCompound,
        heightmap: IHeightmap,
        blockPos: BlockPos,
        biomeProvider: BiomeProvider,
        gutters: List<Rectangle>
    ) {
        val props = NBTTagList()
        // The number of props to generate
        val numberOfProps = Random.nextInt(25, 100)

        for (i in 0 until numberOfProps) {
            // Get a random prop schematic ID
            val schematicId = Random.nextInt(0, ModSchematics.DARK_FOREST_PROPS.size)
            // Get that schematic
            val schematic = ModSchematics.DARK_FOREST_PROPS[schematicId]

            // Get a random gutter to place it in
            val gutter = gutters.random()

            // Compute the x,z position the schematic should be at
            val schematicPosNoY = getRandomPropPosition(schematic, blockPos, gutter)
            val chunkPos = ChunkPos(schematicPosNoY)

            // Figure out if this position is valid (no water under props)
            val biomes = approximateBiomesInChunk(biomeProvider, chunkPos.x, chunkPos.z)
            if (INCOMPATIBLE_BIOMES.any { biomes.contains(it) }) {
                continue
            }

            // Get the low height in the center chunk of the schematic and place the schematic there.
            val yPos = heightmap.getLowestHeight(
                ChunkPos(
                    schematicPosNoY.add(
                        schematic.getWidth() / 2,
                        0,
                        schematic.getLength() / 2
                    )
                )
            )
            // Update the Y value
            val schematicPos = BlockPos(schematicPosNoY.x, yPos, schematicPosNoY.z)

            // Create the prop tag and append it
            val prop = NBTTagCompound()
            prop.setInteger(NBT_SCHEMATIC_ID, schematicId)
            prop.setTag(NBT_POSITION, NBTUtil.createPosTag(schematicPos))
            props.appendTag(prop)
        }
        data.setTag(NBT_PROPS, props)
    }

    /**
     * Gets a random prop position for a schematic and a gutter
     *
     * @param prop     The prop schematic to generate
     * @param blockPos The base position of the schematic
     * @param gutter   The gutter to put the prop in
     * @return The position that this prop will be placed at in the world
     */
    private fun getRandomPropPosition(prop: Schematic, blockPos: BlockPos, gutter: Rectangle): BlockPos {
        // Ensure the schematic fits perfectly into the gutter without going outside
        val xMin = gutter.x
        val xMax = gutter.x + gutter.width - prop.getWidth()
        val zMin = gutter.y
        val zMax = gutter.y + gutter.height - prop.getLength()

        // Return a position in the valid range
        return BlockPos(blockPos.x + Random.nextInt(xMin, xMax), 0, blockPos.z + Random.nextInt(zMin, zMax))
    }

    /**
     * Adds between 10 - 20 random trees in the world around the bed house
     *
     * @param data          The data to add the props to
     * @param heightmap     The heightmap of the world to create trees in
     * @param blockPos      The position to generate the structure at
     * @param gutters       The gutters around the bed house to generate in
     * @param bedHouseSides A parallel list to gutters with the sides that the bed house is at for each gutter
     */
    private fun addRandomTrees(
        data: NBTTagCompound,
        heightmap: IHeightmap,
        blockPos: BlockPos,
        gutters: List<Rectangle>,
        bedHouseSides: List<EnumFacing>
    ) {
        val trees = NBTTagList()

        // The number of trees to generate
        val numberOfTrees = Random.nextInt(10, 20)
        for (i in 0 until numberOfTrees) {
            // Get a random tree schematic ID
            val schematicId = Random.nextInt(0, ModSchematics.DARK_FOREST_TREES.size)
            // Get that schematic
            val schematic = ModSchematics.DARK_FOREST_TREES[schematicId]

            // Get a random gutter to place it in
            val gutterIndex = Random.nextInt(0, gutters.size)
            val gutter = gutters[gutterIndex]
            val bedHouseSide = bedHouseSides[gutterIndex]

            // Compute the x,z position the schematic should be at
            val schematicPosNoY = getRandomTreePosition(schematic, blockPos, gutter, bedHouseSide)
            // Get the low height in the center chunk of the schematic and place the schematic there.
            var yPos = heightmap.getLowestHeight(
                ChunkPos(
                    schematicPosNoY.add(
                        schematic.getWidth() / 2,
                        0,
                        schematic.getLength() / 2
                    )
                )
            )
            // Trees need to have roots underground so move them down by 5, ensure it's above ground though
            yPos = (yPos - 5).coerceIn(0, Int.MAX_VALUE)
            // Update the Y value
            val schematicPos = BlockPos(schematicPosNoY.x, yPos, schematicPosNoY.z)

            // Create the tree tag and append it
            val tree = NBTTagCompound()
            tree.setInteger(NBT_SCHEMATIC_ID, schematicId)
            tree.setTag(NBT_POSITION, NBTUtil.createPosTag(schematicPos))
            trees.appendTag(tree)
        }
        // Set the trees and prop's lists
        data.setTag(NBT_TREES, trees)
    }

    /**
     * Gets a random tree position for a schematic and a gutter
     *
     * @param tree         The tree to generate
     * @param blockPos     The position the structure will be at
     * @param gutter       The gutter to place the tree trunk in
     * @param bedHouseSide The side that the bed house is on relative to the gutter. This is necessary since trees are allowed to
     * overlap with the house since the leaves may go over top
     * @return The position that this tree will be placed at
     */
    private fun getRandomTreePosition(
        tree: Schematic,
        blockPos: BlockPos,
        gutter: Rectangle,
        bedHouseSide: EnumFacing
    ): BlockPos {
        var xMin: Int
        var xMax: Int
        var zMin: Int
        var zMax: Int

        // The amount of space to leave for the trunk
        val trunkSize = 5

        // Compute the min and max positions assuming
        // 1) The tree is not centered
        // 2) The bed house is not on any side
        xMax = gutter.x + gutter.width - tree.getWidth() / 2
        xMin = gutter.x + tree.getWidth() / 2
        zMax = gutter.y + gutter.height - tree.getLength() / 2
        zMin = gutter.y + tree.getLength() / 2

        when (bedHouseSide) {
            EnumFacing.EAST -> xMax = gutter.x + gutter.width - trunkSize
            EnumFacing.WEST -> xMin = gutter.x + trunkSize
            EnumFacing.NORTH -> zMax = gutter.y + gutter.height - trunkSize
            EnumFacing.SOUTH -> zMin = gutter.y + trunkSize
            else -> throw IllegalArgumentException("Expected a side NORTH/SOUTH/EAST/WEST but got '$bedHouseSide'")
        }

        // Move the schematic so that the tree is centered in the position
        xMin = xMin - tree.getWidth() / 2
        xMax = xMax - tree.getWidth() / 2
        zMin = zMin - tree.getLength() / 2
        zMax = zMax - tree.getLength() / 2

        // Return a random valid position
        return BlockPos(blockPos.x + Random.nextInt(xMin, xMax), 0, blockPos.z + Random.nextInt(zMin, zMax))
    }

    /**
     * @return The width of the structure in blocks
     */
    override fun getXWidth(): Int {
        return width
    }

    /**
     * @return The length of the structure in blocks
     */
    override fun getZLength(): Int {
        return height
    }

    companion object {
        // A set of invalid prop biomes
        private val INCOMPATIBLE_BIOMES = setOf(
            Biomes.OCEAN,
            Biomes.DEEP_OCEAN,
            Biomes.FROZEN_OCEAN,
            Biomes.FROZEN_RIVER,
            Biomes.RIVER,
            Biomes.SKY,
            Biomes.VOID
        )

        // A set of compatible biomes
        private val COMPATIBLE_HOUSE_BIOMES = setOf(
            Biomes.SAVANNA,
            Biomes.MUTATED_SAVANNA,
            Biomes.PLAINS,
            Biomes.MUTATED_PLAINS,
            ModBiomes.ERIE_FOREST
        )

        // NBT tag keys
        private const val NBT_TREES = "trees"
        private const val NBT_PROPS = "props"
        private const val NBT_HOUSE_POSITION = "house"
        private const val NBT_SCHEMATIC_ID = "schematic_id"
    }
}