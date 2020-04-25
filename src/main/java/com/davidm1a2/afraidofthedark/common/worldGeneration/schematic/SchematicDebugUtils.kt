package com.davidm1a2.afraidofthedark.common.worldGeneration.schematic

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import net.minecraft.block.Block
import net.minecraft.nbt.*
import net.minecraft.util.ResourceLocation
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.exception.ExceptionUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Collection of utility methods used to debug, not used in actual play
 */
object SchematicDebugUtils {
    /**
     * Sets a block in a schematic
     *
     * @param schematic The schematic to update
     * @param x The block's x position
     * @param y The block's y position
     * @param z The block's z position
     * @param block The block to set it to
     */
    fun setBlock(schematic: Schematic, x: Int, y: Int, z: Int, block: Block) {
        val width = ModSchematics.DESERT_OASIS.getWidth().toInt()
        val length = ModSchematics.DESERT_OASIS.getLength().toInt()

        schematic.getBlocks()[x + y * length * width + z * width] = block
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
            AfraidOfTheDark.INSTANCE.logger.warn("File already exists, returning...")
            return
        }

        try {
            // Create folders down to the file
            file.parentFile.mkdirs()
            // Create the file
            file.createNewFile()

            // Create a schematic NBT
            val schematicNBT = NBTTagCompound()

            // Write each of the w/l/h values to nbt
            schematicNBT.setShort("Width", schematic.getWidth())
            schematicNBT.setShort("Height", schematic.getHeight())
            schematicNBT.setShort("Length", schematic.getLength())

            // Write each entity and tile entity to the nbt
            schematicNBT.setTag("TileEntities", schematic.getTileEntities())
            schematicNBT.setTag("Entities", schematic.getEntities())

            // For each block:
            // 1. Create a map of block name to arbitrary block id
            // 2. Map each block to its arbitrary id
            // 3. Store each arbitrary block id
            // 4. Store the names of the corresponding block ids into an array
            var lastBlockId = 0
            val idToBlock = mutableMapOf<String, Int>()
            val blocks = schematic.getBlocks()
            val blockIds = blocks.map { idToBlock.computeIfAbsent(it.registryName.toString()) { lastBlockId++ } }.toIntArray()
            schematicNBT.setTag("BlockIds", NBTTagIntArray(blockIds))
            schematicNBT.setTag("BlockIdNames", NBTTagList().apply { idToBlock.keys.forEach { appendTag(NBTTagString(it)) } })

            // Write all of the nbt data to disk
            schematicNBT.setIntArray("Data", schematic.getData())

            // Write the nbt to the file
            FileOutputStream(file).use {
                CompressedStreamTools.writeCompressed(schematicNBT, it)
            }
        }
        // Catch the exception and print it out
        catch (e: IOException) {
            AfraidOfTheDark.INSTANCE.logger.error(e)
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
        val nbt = NBTTagCompound()
        nbt.setShort("width", schematic.getWidth())
        nbt.setShort("height", schematic.getHeight())
        nbt.setShort("length", schematic.getLength())

        // Write the NBT to the .meta file
        try {
            FileOutputStream(schematicMetaFile).use {
                CompressedStreamTools.writeCompressed(nbt, it)
            }
        } catch (e: IOException) {
            System.err.println("Could not write schematic .meta file:\n${ExceptionUtils.getStackTrace(e)}")
        }
    }
}