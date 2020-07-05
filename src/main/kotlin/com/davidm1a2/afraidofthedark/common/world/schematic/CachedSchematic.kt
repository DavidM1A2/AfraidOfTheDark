package com.davidm1a2.afraidofthedark.common.world.schematic

import net.minecraft.block.state.IBlockState
import net.minecraft.nbt.NBTTagList

/**
 * Object representation of a schematic file that stores everything in memory. This optimizes for the fastest possible generation time
 *
 * @constructor just initializes fields
 * @property name         The name of the schematic
 * @property tileEntities The tile entities (like chests, etc)
 * @property width        The width of the schematic
 * @property height       The height of the schematic
 * @property length       The length of the schematic
 * @property blocks       The block IDs of each block inside the schematic
 * @property data         The block data of each block inside the schematic
 * @property entities     The entities inside the schematic
 */
class CachedSchematic internal constructor(
    private val name: String,
    private val tileEntities: NBTTagList,
    private val width: Short,
    private val height: Short,
    private val length: Short,
    private val blocks: Array<IBlockState>,
    private val entities: NBTTagList
) : Schematic {
    /**
     * @return The name of the schematic
     */
    override fun getName(): String {
        return name
    }

    /**
     * @return A list of tile entities in the schematic region
     */
    override fun getTileEntities(): NBTTagList {
        return tileEntities
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
    override fun getBlocks(): Array<IBlockState> {
        return blocks
    }

    /**
     * @return A list of entities in the schematic region
     */
    override fun getEntities(): NBTTagList {
        return entities
    }
}