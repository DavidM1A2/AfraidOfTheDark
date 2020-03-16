package com.davidm1a2.afraidofthedark.client.debug

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.Schematic
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicBuilder
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
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

            FileOutputStream(file).use {
                // Create a schematic NBT
                val schematicNBT = NBTTagCompound()

                // Write each of the w/l/h values to nbt
                schematicNBT.setShort("Width", schematic.getWidth())
                schematicNBT.setShort("Height", schematic.getHeight())
                schematicNBT.setShort("Length", schematic.getLength())

                // Write each entity and tile entity to the nbt
                schematicNBT.setTag("TileEntities", schematic.getTileEntities())
                schematicNBT.setTag("Entities", schematic.getEntities())

                // For each block write its name to nbt
                val stringBlocks = NBTTagList()
                for (block in schematic.getBlocks()) {
                    stringBlocks.appendTag(NBTTagString(block.registryName.toString()))
                }
                schematicNBT.setTag("Blocks", stringBlocks)

                // Write all of the nbt data to disk
                schematicNBT.setIntArray("Data", schematic.getData())

                // Write the nbt to the file
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
                generateMcMetaFileForDir(
                    it
                )
            }
            subfiles.filter { it.isFile }.filter { it.name.endsWith(".schematic") }.forEach {
                createMetaFor(
                    it
                )
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
        val schematic =
            SchematicBuilder()
                .withFile(ResourceLocation(Constants.MOD_ID, localPath)).withCacheEnabled(true).build()

        // Create an NBT compound to write to
        val nbt = NBTTagCompound()
        nbt.setShort("width", schematic.getWidth())
        nbt.setShort("height", schematic.getHeight())
        nbt.setShort("length", schematic.getLength())

        // Write the NBT to the .meta file
        try {
            FileOutputStream(schematicMetaFile).use { fileOutputStream ->
                CompressedStreamTools.writeCompressed(
                    nbt,
                    fileOutputStream
                )
            }
        } catch (e: IOException) {
            System.err.println("Could not write schematic .meta file:\n${ExceptionUtils.getStackTrace(e)}")
        }
    }
}