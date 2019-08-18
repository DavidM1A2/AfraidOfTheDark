package com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagList;

/**
 * Object representation of a schematic file that only caches block data in memory when needed. This optimizes for the
 * lowest possible memory usage. Schematics are internally cleared after a timeout.
 */
public class OnDemandSchematic implements Schematic
{

    /**
     * @return A list of tile entities in the schematic region
     */
    @Override
    public NBTTagList getTileEntities()
    {
        return null;
    }

    /**
     * @return The width of the schematic region
     */
    @Override
    public short getWidth()
    {
        return 0;
    }

    /**
     * @return The height of the schematic region
     */
    @Override
    public short getHeight()
    {
        return 0;
    }

    /**
     * @return The length of the schematic region
     */
    @Override
    public short getLength()
    {
        return 0;
    }

    /**
     * @return An array of blocks in the structure
     */
    @Override
    public Block[] getBlocks()
    {
        return null;
    }

    /**
     * @return An array of block metadata values in the schematic
     */
    @Override
    public int[] getData()
    {
        return null;
    }

    /**
     * @return A list of entities in the schematic region
     */
    @Override
    public NBTTagList getEntities()
    {
        return null;
    }
}
