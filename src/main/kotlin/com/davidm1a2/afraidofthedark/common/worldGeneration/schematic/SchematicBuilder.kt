package com.davidm1a2.afraidofthedark.common.worldGeneration.schematic

import com.davidm1a2.afraidofthedark.common.utility.ResourceUtil
import com.electronwill.nightconfig.toml.TomlFormat
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fml.loading.FMLPaths
import org.apache.commons.io.FilenameUtils
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Builds a schematic from a file on disk
 *
 * @constructor Creates a new schematic builder with the default cache enabled
 * @property cacheEnabled True if the schematic should be cached in memory, false if it should be on demand
 * @property resourceLocation The file location of the schematic
 */
class SchematicBuilder {
    private var cacheEnabled = true
    private var resourceLocation: ResourceLocation? = null

    /**
     * Sets the cache enabled flag, true means the schematic data will be stored in memory, false means it will be
     * dynamically loaded when needed
     *
     * @param cacheEnabled True if the cache is enabled false otherwise
     * @return The builder instance
     */
    fun withCacheEnabled(cacheEnabled: Boolean): SchematicBuilder {
        this.cacheEnabled = cacheEnabled
        return this
    }

    /**
     * Sets the file to read the schematic data from
     *
     * @param resourceLocation The schematic resource location
     * @return The builder instance
     */
    fun withFile(resourceLocation: ResourceLocation): SchematicBuilder {
        this.resourceLocation = resourceLocation
        return this
    }

    /**
     * Builds the schematic instance, the resource location must be set
     *
     * @return The schematic ready to be generated in the world
     */
    fun build(): Schematic {
        requireNotNull(resourceLocation) { "Resource location must be specified!" }

        // We can't use ModCommonConfiguration.cacheStructures since that hasn't been loaded yet
        // Instead we use a "hack" to read it from the file early
        return if (USE_CACHE) {
            createCached()
        } else {
            createOnDemand()
        }
    }

    /**
     * Creates a cached schematic that stores all of its data in memory
     *
     * @return A cached schematic instance
     * @throws IOException If the schematic file does not exist or can't be read
     */
    private fun createCached(): Schematic {
        // Grab the name of the schematic
        val schematicName = FilenameUtils.getBaseName(resourceLocation!!.path)
        // Read the NBT data from the file
        val nbtData = ResourceUtil.getInputStream(resourceLocation!!).use {
            CompressedStreamTools.readCompressed(it)
        }

        // Begin processing the data
        val width = nbtData.getShort("Width")
        val height = nbtData.getShort("Height")
        val length = nbtData.getShort("Length")

        // Read the entities and tile entities
        val tileEntities = nbtData.getList("TileEntities", 10)

        val entities = nbtData.getList("Entities", 10)

        // Read the block ids
        val blockIds = nbtData.getIntArray("BlockIds")
        // Read the map of block name to id
        val blockMapData = nbtData.getList("BlockIdData", Constants.NBT.TAG_COMPOUND).map { (it as NBTTagCompound) }

        // Convert block names to block pointer references
        val blockMapBlocks = blockMapData.map { NBTUtil.readBlockState(it) }
        // Map each block id to block pointer
        val blocks = blockIds.map { blockMapBlocks[it] }.toTypedArray()

        // Return the schematic
        return CachedSchematic(schematicName, tileEntities, width, height, length, blocks, entities)
    }

    /**
     * Creates an on demand schematic that dynamically loads its block data when needed
     *
     * @return An on demand schematic instance
     * @throws IOException If the schematic file does not exist or can't be read
     */
    private fun createOnDemand(): Schematic {
        // Grab the name of the schematic
        val schematicName = FilenameUtils.getBaseName(resourceLocation!!.path)
        val metaLocation =
            ResourceLocation(resourceLocation!!.namespace, resourceLocation!!.path + ".meta")
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

    companion object {
        private val LOG = LogManager.getLogger()

        private val USE_CACHE: Boolean

        init {
            val clientConfig = File(FMLPaths.CONFIGDIR.get().toString(), "afraidofthedark-common.toml")
            USE_CACHE = FileInputStream(clientConfig).use {
                try {
                    TomlFormat.instance().createParser().parse(it).get<Boolean>("world_generation.cache_structures")
                } catch (e: Throwable) {
                    LOG.warn("Could not parse 'afraidofthedark-common-toml', defaulting to 'cache_structures = true'")
                    true
                }
            }
        }
    }
}