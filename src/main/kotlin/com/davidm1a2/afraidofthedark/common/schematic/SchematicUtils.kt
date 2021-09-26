package com.davidm1a2.afraidofthedark.common.schematic

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.IntArrayNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.LinkedList
import java.util.UUID

/**
 * Collection of utility methods used to debug, not used in actual play
 */
object SchematicUtils {
    private val logger = LogManager.getLogger()

    fun setBlock(schematic: Schematic, x: Int, y: Int, z: Int, block: BlockState) {
        val width = schematic.getWidth().toInt()
        val length = schematic.getLength().toInt()

        schematic.getBlocks()[x + y * length * width + z * width] = block
    }

    fun getBlock(schematic: Schematic, x: Int, y: Int, z: Int): BlockState {
        val width = schematic.getWidth().toInt()
        val length = schematic.getLength().toInt()

        return schematic.getBlocks()[x + y * length * width + z * width]
    }

    /**
     * Debug method used to write a schematic to disk
     *
     * @param schematic The schematic to write out
     * @param file      The file to write to
     */
    fun writeToFile(schematic: Schematic, file: File) {
        // Don't overwrite files
        if (file.exists()) {
            logger.warn("File already exists, returning...")
            return
        }

        try {
            // Create folders down to the file
            file.parentFile.mkdirs()
            // Create the file
            file.createNewFile()

            // Create a schematic NBT
            val schematicNBT = CompoundNBT()

            // Write each of the w/l/h values to nbt
            schematicNBT.putShort("Width", schematic.getWidth())
            schematicNBT.putShort("Height", schematic.getHeight())
            schematicNBT.putShort("Length", schematic.getLength())

            // Write each entity and tile entity to the nbt
            schematicNBT.put("TileEntities", schematic.getTileEntities())
            schematicNBT.put("Entities", schematic.getEntities())

            // For each block:
            // 1. Create a map of block name to arbitrary block id
            // 2. Map each block to its arbitrary id
            // 3. Store each arbitrary block id
            // 4. Store the names of the corresponding block ids into an array
            var lastBlockId = 0
            val stateToBlock = mutableMapOf<BlockState, Int>()
            val blocks = schematic.getBlocks()
            val blockIds = blocks.map { stateToBlock.computeIfAbsent(it) { lastBlockId++ } }.toIntArray()
            schematicNBT.put("BlockIds", IntArrayNBT(blockIds))
            schematicNBT.put("BlockIdData", ListNBT().apply { stateToBlock.keys.forEach { add(NBTUtil.writeBlockState(it)) } })

            // Write the nbt to the file
            FileOutputStream(file).use {
                CompressedStreamTools.writeCompressed(schematicNBT, it)
            }
        }
        // Catch the exception and print it out
        catch (e: IOException) {
            logger.error(e)
        }
    }

    /**
     * Debug function to create or update existing schematic meta files
     */
    fun createSchematicMetaFiles() {
        generateMcMetaFileForDir(
            File(
                "../src/main/resources/assets/afraidofthedark/schematics"
            )
        )
    }

    /**
     * Generates all the .schematic.mcmeta files for this directory
     *
     * @param schematicDir The directory to generate files in
     */
    private fun generateMcMetaFileForDir(schematicDir: File) {
        // Lists all files the directory
        val subfiles = schematicDir.listFiles()
        if (subfiles != null) {
            // Go over each subfile, the ones that are directories we recurse over, the ones that are schematic files we create a meta file for
            subfiles.filter { it.isDirectory }.forEach {
                generateMcMetaFileForDir(it)
            }
            subfiles.filter { it.isFile }.filter { it.name.endsWith(".schematic") }.forEach {
                createMetaFor(it)
            }
        }
    }

    /**
     * Creates a .meta file for the schematic, if one already exists it's updated
     *
     * @param schematicFile The schematic file to create
     */
    private fun createMetaFor(schematicFile: File) {
        // Create .meta file for the schematic
        val schematicMetaFile = File(schematicFile.absolutePath + ".meta")

        // Delete the existing one
        if (schematicMetaFile.exists()) {
            schematicMetaFile.delete()
        }

        // Get the path to the original schematic file and load it
        val localPath =
            StringUtils.substringAfter(
                schematicFile.absolutePath.replace("\\", "/"),
                "src/main/resources/assets/afraidofthedark/"
            )
        val schematic = SchematicBuilder()
            .withFile(ResourceLocation(Constants.MOD_ID, localPath))
            .withCacheEnabled(true)
            .build()

        // Create an NBT compound to write to
        val nbt = CompoundNBT()
        nbt.putShort("width", schematic.getWidth())
        nbt.putShort("height", schematic.getHeight())
        nbt.putShort("length", schematic.getLength())

        // Write the NBT to the .meta file
        try {
            FileOutputStream(schematicMetaFile).use {
                CompressedStreamTools.writeCompressed(nbt, it)
            }
        } catch (e: IOException) {
            System.err.println("Could not write schematic .meta file:\n${ExceptionUtils.getStackTrace(e)}")
        }
    }

    fun placeRawSchematic(schematic: Schematic, world: World, position: BlockPos) {
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
                    world.setBlockAndUpdate(position.offset(x, y, z), nextToPlace)
                }
            }
        }

        // Get the list of tile entities inside this schematic
        val tileEntities = schematic.getTileEntities()
        // Iterate over each tile entity
        for (i in 0 until tileEntities.size) {
            // Grab the compound that represents this tile entity
            val tileEntityCompound = tileEntities.getCompound(i)
            val relativeTileEntityPosition = BlockPos(tileEntityCompound.getInt("x"), tileEntityCompound.getInt("y"), tileEntityCompound.getInt("z"))
            val absoluteTileEntityPosition = position.offset(relativeTileEntityPosition)
            // Clone the NBT, then update the x, y, and z positions to be world absolute
            val newTileEntityCompound = tileEntityCompound.copy().apply {
                putInt("x", absoluteTileEntityPosition.x)
                putInt("y", absoluteTileEntityPosition.y)
                putInt("z", absoluteTileEntityPosition.z)
            }

            val blockState = getBlock(schematic, relativeTileEntityPosition.x, relativeTileEntityPosition.y, relativeTileEntityPosition.z)
            world.getBlockEntity(absoluteTileEntityPosition)?.load(blockState, newTileEntityCompound)
        }

        // Get the list of entities inside this schematic
        val entities = schematic.getEntities()

        // Iterate over each entity
        for (i in 0 until entities.size) {
            // Grab the compound that represents this entity
            val entityCompound = entities.getCompound(i)
            // Instantiate the entity object from the compound
            val entityOpt = EntityType.create(entityCompound, world)

            // If the entity is valid, continue...
            if (entityOpt.isPresent) {
                val entity = entityOpt.get()
                // Update the UUID to be random so that it does not conflict with other entities from the same schematic
                entity.uuid = UUID.randomUUID()

                // Get the X, Y, and Z coordinates of this entity if instantiated inside the world
                val newX = position.x + entity.x
                val newY = position.y + entity.y
                val newZ = position.z + entity.z

                entity.setPos(newX, newY, newZ)
                world.addFreshEntity(entity)
            }
        }
    }

    fun updateStructureVoids(world: World, position: BlockPos, add: Boolean = true) {
        val blocksToUpdate = mutableSetOf<BlockPos>()
        val queue = LinkedList<BlockPos>()

        queue.add(position)

        val shouldUpdate: (BlockState, BlockPos) -> Boolean = if (add) {
            { state, pos ->
                state.isAir(world, pos)
            }
        } else {
            { state, _ ->
                state.block == Blocks.STRUCTURE_VOID
            }
        }

        while (queue.isNotEmpty() && blocksToUpdate.size < 1000000) {
            val pos = queue.pop()
            if (!blocksToUpdate.contains(pos)) {
                val blockState = world.getBlockState(pos)
                if (shouldUpdate(blockState, pos)) {
                    blocksToUpdate.add(pos)
                    queue.add(pos.above())
                    queue.add(pos.below())
                    queue.add(pos.north())
                    queue.add(pos.south())
                    queue.add(pos.east())
                    queue.add(pos.west())
                }
            }
        }

        if (blocksToUpdate.size < 1000000) {
            if (add) {
                blocksToUpdate.forEach {
                    world.setBlockAndUpdate(it, Blocks.STRUCTURE_VOID.defaultBlockState())
                }
            } else {
                blocksToUpdate.forEach {
                    world.setBlockAndUpdate(it, Blocks.AIR.defaultBlockState())
                }
            }
        } else {
            logger.error("OVERFLOW")
        }
    }
}