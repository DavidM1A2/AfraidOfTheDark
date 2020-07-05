package com.davidm1a2.afraidofthedark.common.world.schematic

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.utility.ResourceUtil
import net.minecraft.block.state.IBlockState
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.util.Constants
import org.apache.logging.log4j.LogManager
import java.io.IOException
import java.util.*

/**
 * Object representation of a schematic file that only caches block data in memory when needed. This optimizes for the
 * lowest possible memory usage. Schematics are internally cleared after a timeout.
 *
 * @constructor initializes all fields that are not loaded on demand and are always cached
 * @property schematicLocation The resource location to load schematic data from when needed
 * @property name The name of the schematic
 * @property width The width of the schematic
 * @property height The height of the schematic
 * @property length The length of the schematic
 * @property lastTimeAccessed The last time this schematic was accessed. Can be null meaning this schematic is not currently cached
 * @property tileEntities A list of tile entities inside the schematic, starts null to avoid load overhead
 * @property blocks The block IDs inside the schematic, starts null to avoid load overhead
 * @property data The raw byte data for each block, starts null to avoid load overhead
 * @property entities A list of entities inside the schematic, starts null to avoid load overhead
 */
class OnDemandSchematic internal constructor(
    private val schematicLocation: ResourceLocation,
    private val name: String,
    private val width: Short,
    private val height: Short,
    private val length: Short
) : Schematic {
    private var lastTimeAccessed: Long? = null
    private var tileEntities: NBTTagList? = null
    private var blocks: Array<IBlockState>? = null
    private var entities: NBTTagList? = null

    init {
        synchronized(ON_DEMAND_SCHEMATICS) { ON_DEMAND_SCHEMATICS.add(this) }
    }

    /**
     * Demands the block data to be cached and ready to go for generation
     */
    @Synchronized
    private fun demandCache() {
        val wasTimedOut = isTimedOut()
        lastTimeAccessed = System.currentTimeMillis()
        if (wasTimedOut) {
            try {
                // Grab an input stream to the schematic file
                val inputStream = ResourceUtil.getInputStream(schematicLocation)
                // Read the NBT data from the file
                val nbtData = CompressedStreamTools.readCompressed(inputStream)

                // Read the entities and tile entities
                tileEntities = nbtData.getList("TileEntities", 10)
                entities = nbtData.getList("Entities", 10)

                // Read the block ids
                val blockIds = nbtData.getIntArray("BlockIds")
                // Read the map of block name to id
                val blockMapData = nbtData.getList("BlockIdData", Constants.NBT.TAG_COMPOUND).map { (it as NBTTagCompound) }

                // Convert block names to block pointer references
                val blockMapBlocks = blockMapData.map { NBTUtil.readBlockState(it) }
                // Map each block id to block pointer
                this.blocks = blockIds.map { blockMapBlocks[it] }.toTypedArray()

                logger.info("Loaded $name into memory.")
            } catch (e: IOException) {
                logger.error("Could not load on-demand schematic $name", e)
            }
        }
    }

    /**
     * Checks the timeout of the schematic and clears the data if it's past the timeout
     */
    @Synchronized
    private fun checkTimeout() {
        if (lastTimeAccessed != null && isTimedOut()) {
            // Timeout, free memory
            lastTimeAccessed = null
            tileEntities = null
            blocks = null
            entities = null
            logger.info("Cleared $name from memory.")
        }
    }

    /**
     * @return True if the schematic is timed out and can be removed from ram
     */
    @Synchronized
    private fun isTimedOut(): Boolean {
        return if (lastTimeAccessed == null) {
            true
        } else System.currentTimeMillis() - lastTimeAccessed!! > ModCommonConfiguration.cacheTimeout
    }

    /**
     * @return The name of the schematic
     */
    override fun getName(): String {
        return name
    }

    /**
     * @return A list of tile entities in the schematic region
     */
    @Synchronized
    override fun getTileEntities(): NBTTagList {
        demandCache()
        return tileEntities!!
    }

    /**
     * @return The width of the schematic region
     */
    override fun getWidth(): Short {
        return width
    }

    /**
     * @return The height of the schematic region
     */
    override fun getHeight(): Short {
        return height
    }

    /**
     * @return The length of the schematic region
     */
    override fun getLength(): Short {
        return length
    }

    /**
     * @return An array of blocks in the structure
     */
    @Synchronized
    override fun getBlocks(): Array<IBlockState> {
        demandCache()
        return blocks!!
    }

    /**
     * @return A list of entities in the schematic region
     */
    @Synchronized
    override fun getEntities(): NBTTagList {
        demandCache()
        return entities!!
    }

    companion object {
        private val logger = LogManager.getLogger()

        // Timer used to test if the schematic is ready to timeout
        private val TIMEOUT_TIMER = Timer("Schematic Cache Timeout Timer")

        // Static list of on demand schematics to test timeouts on
        private val ON_DEMAND_SCHEMATICS = mutableListOf<OnDemandSchematic>()

        init {
            TIMEOUT_TIMER.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    synchronized(ON_DEMAND_SCHEMATICS) { ON_DEMAND_SCHEMATICS.forEach { it.checkTimeout() } }
                }
            }, 0, 5000)
        }
    }
}