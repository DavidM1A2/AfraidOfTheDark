package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import com.davidm1a2.afraidofthedark.common.world.schematic.Schematic
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ChestBlock
import net.minecraft.block.StairsBlock
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.state.properties.ChestType
import net.minecraft.state.properties.StairsShape
import net.minecraft.tileentity.ChestTileEntity
import net.minecraft.util.Direction
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.gen.feature.structure.StructurePiece
import java.util.*

class SchematicStructurePiece : StructurePiece {
    private val schematic: Schematic
    private val lootTable: LootTable?
    private var mirror = Mirror.NONE

    // Some blocks aren't mirrored correctly by MC. Fix them here
    private val mirrorBlockFixer = mapOf<Block, (BlockState) -> BlockState>(
        Blocks.CHEST to { state ->
            when (state[ChestBlock.TYPE]) {
                ChestType.RIGHT -> state.with(ChestBlock.TYPE, ChestType.LEFT)
                ChestType.LEFT -> state.with(ChestBlock.TYPE, ChestType.RIGHT)
                else -> state
            }
        },
        *setOf(
            Blocks.ACACIA_STAIRS,
            Blocks.BIRCH_STAIRS,
            Blocks.OAK_STAIRS,
            Blocks.SPRUCE_STAIRS,
            Blocks.DARK_OAK_STAIRS,
            Blocks.JUNGLE_STAIRS,
            Blocks.PRISMARINE_BRICK_STAIRS,
            Blocks.NETHER_BRICK_STAIRS,
            Blocks.DARK_PRISMARINE_STAIRS,
            Blocks.COBBLESTONE_STAIRS,
            Blocks.BRICK_STAIRS,
            Blocks.PRISMARINE_STAIRS,
            Blocks.PURPUR_STAIRS,
            Blocks.QUARTZ_STAIRS,
            Blocks.SANDSTONE_STAIRS,
            Blocks.RED_SANDSTONE_STAIRS,
            Blocks.STONE_BRICK_STAIRS,
            ModBlocks.GRAVEWOOD_STAIRS,
            ModBlocks.MANGROVE_STAIRS,
            ModBlocks.SACRED_MANGROVE_STAIRS
        ).map { it to this::fixStairState }.toTypedArray()
    )

    constructor(nbt: CompoundNBT) : super(ModStructures.SCHEMATIC_STRUCTURE_PIECE, nbt) {
        val schematicName = nbt.getString(NBT_SCHEMATIC_NAME)
        this.schematic = ModSchematics.NAME_TO_SCHEMATIC[schematicName] ?: throw IllegalStateException("Schematic $schematicName was not found")
        if (nbt.contains(NBT_LOOT_TABLE_NAME)) {
            val lootTableName = nbt.getString(NBT_LOOT_TABLE_NAME)
            this.lootTable = ModLootTables.NAME_TO_LOOT_TABLE[lootTableName] ?: throw IllegalStateException("LootTable $lootTableName was not found")
        } else {
            this.lootTable = null
        }
    }

    constructor(
        x: Int,
        y: Int,
        z: Int,
        random: Random,
        schematic: Schematic,
        lootTable: LootTable? = null,
        facing: Direction? = null
    ) : super(ModStructures.SCHEMATIC_STRUCTURE_PIECE, 0) {
        this.schematic = schematic
        this.lootTable = lootTable

        require(facing == null || facing in Direction.Plane.HORIZONTAL)

        // The first random number is always 0. No idea why
        random.nextInt(4)
        coordBaseMode = facing ?: Direction.Plane.HORIZONTAL.random(random)
        boundingBox = if (coordBaseMode?.axis == Direction.Axis.Z) {
            MutableBoundingBox(x, y, z, x + schematic.getWidth() - 1, y + schematic.getHeight() - 1, z + schematic.getLength() - 1)
        } else {
            MutableBoundingBox(x, y, z, x + schematic.getLength() - 1, y + schematic.getHeight() - 1, z + schematic.getWidth() - 1)
        }
    }

    fun updateY(y: Int) {
        boundingBox.minY = y
        boundingBox.maxY = y + schematic.getHeight() - 1
    }

    // Copy & Pasted from StructurePiece, used to expose the rotation and mirror fields
    override fun setCoordBaseMode(facing: Direction?) {
        super.setCoordBaseMode(facing)
        if (facing == null) {
            this.mirror = Mirror.NONE
        } else {
            when (facing) {
                Direction.SOUTH -> {
                    this.mirror = Mirror.LEFT_RIGHT
                }
                Direction.WEST -> {
                    this.mirror = Mirror.LEFT_RIGHT
                }
                Direction.EAST -> {
                    this.mirror = Mirror.NONE
                }
                else -> {
                    this.mirror = Mirror.NONE
                }
            }
        }
    }

    /**
     * Actually writeAdditional LOL
     *
     * @param tagCompound The tag to write to
     */
    override fun readAdditional(tagCompound: CompoundNBT) {
        tagCompound.putString(NBT_SCHEMATIC_NAME, this.schematic.getName())
        this.lootTable?.let { tagCompound.putString(NBT_LOOT_TABLE_NAME, it.name) }
    }

    override fun addComponentParts(world: IWorld, random: Random, structureBoundingBox: MutableBoundingBox, chunkPos: ChunkPos): Boolean {
        generateBlocks(world, structureBoundingBox)
        generateTileEntities(world, random, structureBoundingBox)
        generateEntities(world, structureBoundingBox)
        return true
    }

    private fun generateBlocks(world: IWorld, structureBoundingBox: MutableBoundingBox) {
        val blocks = schematic.getBlocks()
        val width = schematic.getWidth()
        val height = schematic.getHeight()
        val length = schematic.getLength()

        val posX = 0
        val posY = 0
        val posZ = 0
        val endX = width.toInt()
        val endY = height.toInt()
        val endZ = length.toInt()

        // Iterate over the Y axis first since that's the format that schematics use
        for (y in posY until endY) {
            // Get the y index which we can use to index into the blocks array
            val indexY = (y - posY) * length * width

            // Iterate over the Z axis second
            for (z in posZ until endZ) {
                // Get the z index which we can use to index into the blocks array
                val indexZ = (z - posZ) * width

                // Iterate over the X axis last
                for (x in posX until endX) {
                    // Get the x index which we can use to index into the blocks array
                    val indexX = x - posX

                    // Compute the index into our blocks array
                    val index = indexY + indexZ + indexX

                    // Grab the reference to the next block to place
                    val nextToPlace = blocks[index]

                    // If the block in the schematic is air then ignore it
                    if (!nextToPlace.isAir(world, BlockPos(x, y, z))) {
                        // Structure void blocks represent air blocks in my schematic system. This allows for easy underground structure generation.
                        if (nextToPlace.block == Blocks.STRUCTURE_VOID) {
                            // Set the block to air
                            setBlockState(world, Blocks.AIR.defaultState, x, y, length - z - 1, structureBoundingBox)
                        } else if (mirror == Mirror.LEFT_RIGHT && mirrorBlockFixer.containsKey(nextToPlace.block)) {
                            // Set the block state after applying the fixer
                            setBlockState(world, mirrorBlockFixer[nextToPlace.block]!!.invoke(nextToPlace), x, y, length - z - 1, structureBoundingBox)
                        } else {
                            // Set the block state without any fix
                            setBlockState(world, nextToPlace, x, y, length - z - 1, structureBoundingBox)
                        }
                    }
                }
            }
        }
    }

    private fun generateTileEntities(world: IWorld, random: Random, structureBoundingBox: MutableBoundingBox) {
        // Get the list of tile entities inside this schematic
        val tileEntities = schematic.getTileEntities()
        val length = schematic.getLength()
        // Iterate over each tile entity
        for (i in 0 until tileEntities.size) {
            // Grab the compound that represents this tile entity
            val tileEntityCompound = tileEntities.getCompound(i)
            val posX = tileEntityCompound.getInt("x")
            val posY = tileEntityCompound.getInt("y")
            val posZ = tileEntityCompound.getInt("z")
            val tileEntityPosition = BlockPos(
                this.getXWithOffset(posX, length - posZ - 1),
                this.getYWithOffset(posY),
                this.getZWithOffset(posX, length - posZ - 1)
            )

            // If the chunk pos was not given or we are in the correct chunk spawn the entity in
            if (structureBoundingBox.isVecInside(tileEntityPosition)) {
                val tileEntity = world.getTileEntity(tileEntityPosition)

                if (tileEntity != null) {
                    val newTileEntityCompound = tileEntityCompound.copy().apply {
                        putInt("x", tileEntityPosition.x)
                        putInt("y", tileEntityPosition.y)
                        putInt("z", tileEntityPosition.z)
                    }

                    tileEntity.read(newTileEntityCompound)

                    if (mirror != Mirror.NONE) {
                        tileEntity.mirror(mirror)
                    }
                    if (rotation != Rotation.NONE) {
                        tileEntity.rotate(rotation)
                    }

                    // If the tile entity is a chest and we have a loot table then generate the chest
                    if (tileEntity is ChestTileEntity) {
                        lootTable?.generate(tileEntity, tileEntityCompound, random)
                    }
                }
            }
        }
    }

    private fun generateEntities(world: IWorld, structureBoundingBox: MutableBoundingBox) {
        // Get the list of entities inside this schematic
        val entities = schematic.getEntities()

        // Iterate over each entity
        for (i in 0 until entities.size) {
            // Grab the compound that represents this entity
            val entityCompound = entities.getCompound(i)
            // Instantiate the entity object from the compound
            val entityOpt = EntityType.loadEntityUnchecked(entityCompound, world.world)

            // If the entity is valid, continue...
            if (entityOpt.isPresent) {
                val entity = entityOpt.get()
                // Update the UUID to be random so that it does not conflict with other entities from the same schematic
                entity.setUniqueId(UUID.randomUUID())
                val length = schematic.getLength()

                // Get the X, Y, and Z coordinates of this entity if instantiated inside the world
                val newX = getXWithOffset(entity.posX, length - entity.posZ - 1)
                val newY = getYWithOffset(entity.posY)
                val newZ = getZWithOffset(entity.posX, length - entity.posZ - 1)

                // If the chunk pos was not given or we are in the correct chunk spawn the entity in
                if (structureBoundingBox.isVecInside(BlockPos(newX, newY, newZ))) {
                    entity.setPosition(newX, newY, newZ)
                    world.addEntity(entity)
                }
            }
        }
    }

    private fun fixStairState(state: BlockState): BlockState {
        val facing = state[StairsBlock.FACING]
        if (facing.axis == Direction.Axis.X) {
            return when (state[StairsBlock.SHAPE]) {
                StairsShape.INNER_LEFT -> state.with(StairsBlock.SHAPE, StairsShape.INNER_RIGHT)
                StairsShape.INNER_RIGHT -> state.with(StairsBlock.SHAPE, StairsShape.INNER_LEFT)
                StairsShape.OUTER_LEFT -> state.with(StairsBlock.SHAPE, StairsShape.OUTER_RIGHT)
                StairsShape.OUTER_RIGHT -> state.with(StairsBlock.SHAPE, StairsShape.OUTER_LEFT)
                else -> state
            }
        }
        return state
    }

    private fun getXWithOffset(x: Double, z: Double): Double {
        return if (coordBaseMode == null) {
            x
        } else {
            when (coordBaseMode) {
                Direction.NORTH, Direction.SOUTH -> boundingBox.minX + x
                Direction.WEST -> boundingBox.maxX - z
                Direction.EAST -> boundingBox.minX + z
                else -> x
            }
        }
    }

    private fun getYWithOffset(y: Double): Double {
        return if (coordBaseMode == null) y else y + boundingBox.minY
    }

    private fun getZWithOffset(x: Double, z: Double): Double {
        return if (coordBaseMode == null) {
            z
        } else {
            when (coordBaseMode) {
                Direction.NORTH -> boundingBox.maxZ - z
                Direction.SOUTH -> boundingBox.minZ + z
                Direction.WEST, Direction.EAST -> boundingBox.minZ + x
                else -> z
            }
        }
    }

    companion object {
        private const val NBT_SCHEMATIC_NAME = "schematic_name"
        private const val NBT_LOOT_TABLE_NAME = "loot_table_name"
    }
}