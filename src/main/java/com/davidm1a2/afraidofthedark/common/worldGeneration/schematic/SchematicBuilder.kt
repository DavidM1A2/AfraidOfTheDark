package com.davidm1a2.afraidofthedark.common.worldGeneration.schematic

import com.davidm1a2.afraidofthedark.common.utility.ResourceUtil
import net.minecraft.block.Block
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.util.ResourceLocation
import org.apache.commons.io.FilenameUtils
import java.io.IOException

/**
 * Builds a schematic from a file on disk
 *
 * @constructor Creates a new schematic builder with the default cache enabled
 * @property cacheEnabled True if the schematic should be cached in memory, false if it should be on demand
 * @property resourceLocation The file location of the schematic
 */
class SchematicBuilder
{
    private var cacheEnabled = true
    private var resourceLocation: ResourceLocation? = null

    /**
     * Sets the cache enabled flag, true means the schematic data will be stored in memory, false means it will be
     * dynamically loaded when needed
     *
     * @param cacheEnabled True if the cache is enabled false otherwise
     * @return The builder instance
     */
    fun withCacheEnabled(cacheEnabled: Boolean): SchematicBuilder
    {
        this.cacheEnabled = cacheEnabled
        return this
    }

    /**
     * Sets the file to read the schematic data from
     *
     * @param resourceLocation The schematic resource location
     * @return The builder instance
     */
    fun withFile(resourceLocation: ResourceLocation): SchematicBuilder
    {
        this.resourceLocation = resourceLocation
        return this
    }

    /**
     * Builds the schematic instance, the resource location must be set
     *
     * @return The schematic ready to be generated in the world
     */
    fun build(): Schematic
    {
        requireNotNull(resourceLocation) { "Resource location must be specified!" }

        return if (cacheEnabled)
        {
            createCached()
        }
        else
        {
            createOnDemand()
        }
    }

    /**
     * Creates a cached schematic that stores all of its data in memory
     *
     * @return A cached schematic instance
     * @throws IOException If the schematic file does not exist or can't be read
     */
    private fun createCached(): Schematic
    {
        // Grab the name of the schematic
        val schematicName = FilenameUtils.getBaseName(resourceLocation!!.resourcePath)
        // Grab an input stream to the schematic file
        val inputStream = ResourceUtil.getInputStream(resourceLocation!!)
        // Read the NBT data from the file
        val nbtData = CompressedStreamTools.readCompressed(inputStream)

        // Begin processing the data
        val width = nbtData.getShort("Width")
        val height = nbtData.getShort("Height")
        val length = nbtData.getShort("Length")

        // Read the entities and tile entities
        val tileEntities = nbtData.getTagList("TileEntities", 10)
        val entities = nbtData.getTagList("Entities", 10)

        // Read the blocks and data, use type 8 for string data.
        val stringBlocks = nbtData.getTagList("Blocks", 8)
        val data = nbtData.getIntArray("Data")

        // Convert all of our string blocks in the format of 'modid:registryname' to block pointer
        val blocks = Array(stringBlocks.tagCount())
        {
            Block.getBlockFromName(stringBlocks.getStringTagAt(it)) ?: throw IllegalStateException("Invalid schematic block found: ${stringBlocks.getStringTagAt(it)}")
        }

        // Return the schematic
        return CachedSchematic(schematicName, tileEntities, width, height, length, blocks, data, entities)
    }

    /**
     * Creates an on demand schematic that dynamically loads its block data when needed
     *
     * @return An on demand schematic instance
     * @throws IOException If the schematic file does not exist or can't be read
     */
    private fun createOnDemand(): Schematic
    {
        // Grab the name of the schematic
        val schematicName = FilenameUtils.getBaseName(resourceLocation!!.resourcePath)
        val metaLocation = ResourceLocation(resourceLocation!!.resourceDomain, resourceLocation!!.resourcePath + ".meta")
        // Grab an input stream to the schematic meta file
        val inputStream = ResourceUtil.getInputStream(metaLocation)
        // Read the NBT data from the file
        val nbtData = CompressedStreamTools.readCompressed(inputStream)

        // Grab the 3 fields stored in the .meta field
        val width = nbtData.getShort("width")
        val height = nbtData.getShort("height")
        val length = nbtData.getShort("length")

        // Return the schematic
        return OnDemandSchematic(resourceLocation!!, schematicName, width, height, length)
    }
}