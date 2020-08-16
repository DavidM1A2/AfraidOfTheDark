package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.schematic.Schematic
import net.minecraft.block.Block
import net.minecraft.block.BlockChest
import net.minecraft.block.BlockStairs
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityType
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.state.properties.ChestType
import net.minecraft.state.properties.StairsShape
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.EnumFacing
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.gen.feature.structure.StructurePiece
import net.minecraft.world.gen.feature.template.TemplateManager
import java.util.*

class SchematicStructurePiece() : StructurePiece() {
    private lateinit var schematic: Schematic
    private var lootTable: LootTable? = null
    private var mirror = Mirror.NONE
    private var rotation: Rotation = Rotation.NONE

    // Some blocks aren't mirrored correctly by MC. Fix them here
    private val mirrorBlockFixer = mapOf<Block, (IBlockState) -> IBlockState>(
        Blocks.CHEST to { state ->
            when (state[BlockChest.TYPE]) {
                ChestType.RIGHT -> state.with(BlockChest.TYPE, ChestType.LEFT)
                ChestType.LEFT -> state.with(BlockChest.TYPE, ChestType.RIGHT)
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

    constructor(x: Int, y: Int, z: Int, random: Random, schematic: Schematic, lootTable: LootTable? = null, facing: EnumFacing? = null) : this() {
        this.schematic = schematic
        this.lootTable = lootTable

        require(facing == null || facing in EnumFacing.Plane.HORIZONTAL)

        // The first random number is always 0. No idea why
        random.nextInt(4)
        coordBaseMode = facing ?: EnumFacing.Plane.HORIZONTAL.random(random)
        boundingBox = if (coordBaseMode?.axis == EnumFacing.Axis.Z) {
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
    override fun setCoordBaseMode(facing: EnumFacing?) {
        super.setCoordBaseMode(facing)
        if (facing == null) {
            this.rotation = Rotation.NONE
            this.mirror = Mirror.NONE
        } else {
            when (facing) {
                EnumFacing.SOUTH -> {
                    this.mirror = Mirror.LEFT_RIGHT
                    this.rotation = Rotation.NONE
                }
                EnumFacing.WEST -> {
                    this.mirror = Mirror.LEFT_RIGHT
                    this.rotation = Rotation.CLOCKWISE_90
                }
                EnumFacing.EAST -> {
                    this.mirror = Mirror.NONE
                    this.rotation = Rotation.CLOCKWISE_90
                }
                else -> {
                    this.mirror = Mirror.NONE
                    this.rotation = Rotation.NONE
                }
            }
        }
    }

    override fun writeStructureToNBT(nbt: NBTTagCompound) {
        nbt.setString(NBT_SCHEMATIC_NAME, this.schematic.getName())
        this.lootTable?.let { nbt.setString(NBT_LOOT_TABLE_NAME, it.name) }
    }

    override fun readStructureFromNBT(nbt: NBTTagCompound, ignored: TemplateManager) {
        val schematicName = nbt.getString(NBT_SCHEMATIC_NAME)
        this.schematic = ModSchematics.NAME_TO_SCHEMATIC[schematicName] ?: throw IllegalStateException("Schematic $schematicName was not found")
        if (nbt.hasKey(NBT_LOOT_TABLE_NAME)) {
            val lootTableName = nbt.getString(NBT_LOOT_TABLE_NAME)
            this.lootTable = ModLootTables.NAME_TO_LOOT_TABLE[lootTableName] ?: throw IllegalStateException("LootTable $lootTableName was not found")
        } else {
            this.lootTable = null
        }
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
                    tileEntity.read(tileEntityCompound)

                    tileEntity.pos = tileEntityPosition
                    if (mirror != Mirror.NONE) {
                        tileEntity.mirror(mirror)
                    }
                    if (rotation != Rotation.NONE) {
                        tileEntity.rotate(rotation)
                    }

                    // If the tile entity is a chest and we have a loot table then generate the chest
                    if (tileEntity is TileEntityChest) {
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
            val entity = EntityType.create(entityCompound, world.world)

            // If the entity is valid, continue...
            if (entity != null) {
                // Update the UUID to be random so that it does not conflict with other entities from the same schematic
                entity.setUniqueId(UUID.randomUUID())

                // Get the X, Y, and Z coordinates of this entity if instantiated inside the world
                val newX = entity.posX + structureBoundingBox.minX
                val newY = entity.posY + structureBoundingBox.minY
                val newZ = entity.posZ + structureBoundingBox.minZ

                // If the chunk pos was not given or we are in the correct chunk spawn the entity in
                if (structureBoundingBox.isVecInside(BlockPos(newX, newY, newZ))) {
                    entity.setPosition(newX, newY, newZ)
                    world.spawnEntity(entity)
                }
            }
        }
    }

    private fun fixStairState(state: IBlockState): IBlockState {
        val facing = state[BlockStairs.FACING]
        if (facing.axis == EnumFacing.Axis.X) {
            return when (state[BlockStairs.SHAPE]) {
                StairsShape.INNER_LEFT -> state.with(BlockStairs.SHAPE, StairsShape.INNER_RIGHT)
                StairsShape.INNER_RIGHT -> state.with(BlockStairs.SHAPE, StairsShape.INNER_LEFT)
                StairsShape.OUTER_LEFT -> state.with(BlockStairs.SHAPE, StairsShape.OUTER_RIGHT)
                StairsShape.OUTER_RIGHT -> state.with(BlockStairs.SHAPE, StairsShape.OUTER_LEFT)
                else -> state
            }
        }
        return state
    }

    companion object {
        private const val NBT_SCHEMATIC_NAME = "schematic_name"
        private const val NBT_LOOT_TABLE_NAME = "loot_table_name"
    }
}