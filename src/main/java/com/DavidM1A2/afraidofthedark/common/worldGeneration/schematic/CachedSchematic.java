package com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagList;

/**
 * Object representation of a schematic file that stores everything in memory. This optimizes for the fastest possible generation time
 */
public class CachedSchematic implements Schematic
{
    // A list of tile entities inside the schematic
    private final NBTTagList tileEntities;
    // The width of the schematic
    private final short width;
    // The height of the schematic
    private final short height;
    // The length of the schematic
    private final short length;
    // The block IDs inside the schematic
    private final Block[] blocks;
    // The raw byte data for each block
    private final int[] data;
    // A list of entities inside the schematic
    private final NBTTagList entities;

    /**
     * Constructor just initializes fields
     *
     * @param tileEntities The tile entities (like chests, etc)
     * @param width        The width of the schematic
     * @param height       The height of the schematic
     * @param length       The length of the schematic
     * @param blocks       The block IDs of each block inside the schematic
     * @param data         The block data of each block inside the schematic
     * @param entities     The entities inside the schematic
     */
    CachedSchematic(NBTTagList tileEntities, short width, short height, short length, Block[] blocks, int[] data, NBTTagList entities)
    {
        this.tileEntities = tileEntities;
        this.width = width;
        this.height = height;
        this.length = length;
        this.blocks = blocks;
        this.data = data;
        this.entities = entities;
    }

    /**
     * @return A list of tile entities in the schematic region
     */
    @Override
    public NBTTagList getTileEntities()
    {
        return tileEntities;
    }

    /**
     * @return The width of the schematic region
     */
    @Override
    public short getWidth()
    {
        return width;
    }

    /**
     * @return The height of the schematic region
     */
    @Override
    public short getHeight()
    {
        return height;
    }

    /**
     * @return The length of the schematic region
     */
    @Override
    public short getLength()
    {
        return length;
    }

    /**
     * @return An array of blocks in the structure
     */
    @Override
    public Block[] getBlocks()
    {
        return blocks;
    }

    /**
     * @return An array of block metadata values in the schematic
     */
    @Override
    public int[] getData()
    {
        return data;
    }

    /**
     * @return A list of entities in the schematic region
     */
    @Override
    public NBTTagList getEntities()
    {
        return this.entities;
    }
}
