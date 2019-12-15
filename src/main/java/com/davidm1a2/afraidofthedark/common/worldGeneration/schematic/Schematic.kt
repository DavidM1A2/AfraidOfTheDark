package com.davidm1a2.afraidofthedark.common.worldGeneration.schematic

import net.minecraft.block.Block
import net.minecraft.nbt.NBTTagList

/**
 * Object representation of a schematic file (.schematic)
 */
interface Schematic
{
    /**
     * @return The name of the schematic
     */
    fun getName(): String

    /**
     * @return A list of tile entities in the schematic region
     */
    fun getTileEntities(): NBTTagList

    /**
     * @return The width of the schematic region
     */
    fun getWidth(): Short

    /**
     * @return The height of the schematic region
     */
    fun getHeight(): Short

    /**
     * @return The length of the schematic region
     */
    fun getLength(): Short

    /**
     * @return An array of blocks in the structure
     */
    fun getBlocks(): Array<Block>

    /**
     * @return An array of block metadata values in the schematic
     */
    fun getData(): IntArray

    /**
     * @return A list of entities in the schematic region
     */
    fun getEntities(): NBTTagList
}