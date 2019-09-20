package com.davidm1a2.afraidofthedark.common.worldGeneration.schematic;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagList;

/**
 * Object representation of a schematic file (.schematic)
 */
public interface Schematic
{
    /**
     * @return The name of the schematic
     */
    String getName();

    /**
     * @return A list of tile entities in the schematic region
     */
    NBTTagList getTileEntities();

    /**
     * @return The width of the schematic region
     */
    short getWidth();

    /**
     * @return The height of the schematic region
     */
    short getHeight();

    /**
     * @return The length of the schematic region
     */
    short getLength();

    /**
     * @return An array of blocks in the structure
     */
    Block[] getBlocks();

    /**
     * @return An array of block metadata values in the schematic
     */
    int[] getData();

    /**
     * @return A list of entities in the schematic region
     */
    NBTTagList getEntities();
}
