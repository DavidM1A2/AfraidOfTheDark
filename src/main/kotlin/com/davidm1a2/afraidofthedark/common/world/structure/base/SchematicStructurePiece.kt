package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import com.davidm1a2.afraidofthedark.common.loot.loottable.LootTable
import com.davidm1a2.afraidofthedark.common.schematic.Schematic
import com.davidm1a2.afraidofthedark.common.schematic.SchematicUtils
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.StructureFeatureManager
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.ChestBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.StairsShape
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.StructurePiece
import net.minecraftforge.registries.ForgeRegistries
import java.util.*

class SchematicStructurePiece : StructurePiece {
    private val schematic: Schematic
    private val lootTable: LootTable?
    private var mirror = Mirror.NONE

    // Some blocks aren't mirrored correctly by MC. Fix them here
    private val mirrorBlockFixer: Map<Block, (BlockState) -> BlockState> by lazy {
        ForgeRegistries.BLOCKS
            .filterIsInstance<StairBlock>()
            .associateWith { this::fixStairState }
    }

    constructor(nbt: CompoundTag) : super(ModStructures.SCHEMATIC_STRUCTURE_PIECE, nbt) {
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
        facing: Direction = getRandomHorizontalDirection(random)
    ) : super(ModStructures.SCHEMATIC_STRUCTURE_PIECE, 0, makeBoundingBox(x, y, z, facing,
            x + schematic.getWidth() - 1, y + schematic.getHeight() - 1, z + schematic.getLength() - 1)) {
        require(facing in Direction.Plane.HORIZONTAL)

        this.schematic = schematic
        this.lootTable = lootTable
        this.orientation = facing
    }

    fun updateY(y: Int) {
        boundingBox.move(0, y - boundingBox.minY(), 0)
    }

    // Copy & Pasted from StructurePiece, used to expose the rotation and mirror fields
    override fun setOrientation(facing: Direction?) {
        super.setOrientation(facing)
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

    override fun addAdditionalSaveData(serverLevel: ServerLevel, tagCompound: CompoundTag) {
        tagCompound.putString(NBT_SCHEMATIC_NAME, this.schematic.getName())
        this.lootTable?.let { tagCompound.putString(NBT_LOOT_TABLE_NAME, it.name) }
    }

    override fun postProcess(
        world: WorldGenLevel,
        structureManager: StructureFeatureManager,
        chunkGenerator: ChunkGenerator,
        random: Random,
        structureBoundingBox: BoundingBox,
        chunkPos: ChunkPos,
        structureBottomCenter: BlockPos
    ): Boolean {
        generateBlocks(world, structureBoundingBox)
        generateTileEntities(world, random, structureBoundingBox)
        generateEntities(world, structureBoundingBox)
        return true
    }

    private fun generateBlocks(world: WorldGenLevel, structureBoundingBox: BoundingBox) {
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
                    if (!nextToPlace.isAir) {
                        // Structure void blocks represent air blocks in my schematic system. This allows for easy underground structure generation.
                        if (nextToPlace.block == Blocks.STRUCTURE_VOID) {
                            // Set the block to air
                            placeBlock(world, Blocks.AIR.defaultBlockState(), x, y, length - z - 1, structureBoundingBox)
                        } else if (mirror == Mirror.LEFT_RIGHT && mirrorBlockFixer.containsKey(nextToPlace.block)) {
                            // Set the block state after applying the fixer
                            placeBlock(world, mirrorBlockFixer[nextToPlace.block]!!.invoke(nextToPlace), x, y, length - z - 1, structureBoundingBox)
                        } else {
                            // Set the block state without any fix
                            placeBlock(world, nextToPlace, x, y, length - z - 1, structureBoundingBox)
                        }
                    }
                }
            }
        }
    }

    private fun generateTileEntities(world: WorldGenLevel, random: Random, structureBoundingBox: BoundingBox) {
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
                this.getWorldX(posX, length - posZ - 1),
                this.getWorldY(posY),
                this.getWorldZ(posX, length - posZ - 1)
            )

            // If the chunk pos was not given or we are in the correct chunk spawn the entity in
            if (structureBoundingBox.isInside(tileEntityPosition)) {
                val tileEntity = world.getBlockEntity(tileEntityPosition)

                if (tileEntity != null) {
                    val newTileEntityCompound = tileEntityCompound.copy().apply {
                        putInt("x", tileEntityPosition.x)
                        putInt("y", tileEntityPosition.y)
                        putInt("z", tileEntityPosition.z)
                    }

                    tileEntity.load(newTileEntityCompound)

                    if (mirror != Mirror.NONE) {
                        tileEntity.blockState.mirror(mirror)
                    }
                    if (rotation != Rotation.NONE) {
                        tileEntity.blockState.rotate(rotation)
                    }

                    // If the tile entity is a chest and we have a loot table then generate the chest
                    if (tileEntity is ChestBlockEntity) {
                        lootTable?.generate(tileEntity, tileEntityCompound, random)
                    }
                }
            }
        }
    }

    private fun generateEntities(world: WorldGenLevel, structureBoundingBox: BoundingBox) {
        // Get the list of entities inside this schematic
        val entities = schematic.getEntities()

        // Iterate over each entity
        for (i in 0 until entities.size) {
            // Grab the compound that represents this entity
            val entityCompound = entities.getCompound(i)
            // Instantiate the entity object from the compound
            val entityOpt = EntityType.create(entityCompound, world.level)

            // If the entity is valid, continue...
            if (entityOpt.isPresent) {
                val entity = entityOpt.get()
                // Update the UUID to be random so that it does not conflict with other entities from the same schematic
                entity.uuid = UUID.randomUUID()
                val length = schematic.getLength()

                // Get the X, Y, and Z coordinates of this entity if instantiated inside the world
                val newX = getWorldX(entity.x, length - entity.z - 1)
                val newY = getWorldY(entity.y)
                val newZ = getWorldZ(entity.x, length - entity.z - 1)

                // If the chunk pos was not given or we are in the correct chunk spawn the entity in
                if (structureBoundingBox.isInside(BlockPos(newX, newY, newZ))) {
                    entity.setPos(newX, newY, newZ)
                    world.addFreshEntity(entity)
                }
            }
        }
    }

    private fun getWorldX(x: Double, z: Double): Double {
        return if (orientation == null) {
            x
        } else {
            when (orientation) {
                Direction.NORTH, Direction.SOUTH -> boundingBox.minX() + x
                Direction.WEST -> boundingBox.maxX() - z
                Direction.EAST -> boundingBox.minX() + z
                else -> x
            }
        }
    }

    private fun getWorldY(y: Double): Double {
        return if (orientation == null) y else y + boundingBox.minY()
    }

    private fun getWorldZ(x: Double, z: Double): Double {
        return if (orientation == null) {
            z
        } else {
            when (orientation) {
                Direction.NORTH -> boundingBox.maxZ() - z
                Direction.SOUTH -> boundingBox.minZ() + z
                Direction.WEST, Direction.EAST -> boundingBox.minZ() + x
                else -> z
            }
        }
    }

    private fun fixStairState(state: BlockState): BlockState {
        val facing = state.getValue(StairBlock.FACING)
        if (facing.axis == Direction.Axis.X) {
            return when (state.getValue(StairBlock.SHAPE)) {
                StairsShape.INNER_LEFT -> state.setValue(StairBlock.SHAPE, StairsShape.INNER_RIGHT)
                StairsShape.INNER_RIGHT -> state.setValue(StairBlock.SHAPE, StairsShape.INNER_LEFT)
                StairsShape.OUTER_LEFT -> state.setValue(StairBlock.SHAPE, StairsShape.OUTER_RIGHT)
                StairsShape.OUTER_RIGHT -> state.setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT)
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