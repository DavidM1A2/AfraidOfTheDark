package com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.utility.ResourceUtil;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Object representation of a schematic file that only caches block data in memory when needed. This optimizes for the
 * lowest possible memory usage. Schematics are internally cleared after a timeout.
 */
public class OnDemandSchematic implements Schematic
{
    // Timer used to test if the schematic is ready to timeout
    private static final Timer TIMEOUT_TIMER = new Timer("Schematic Timeout Timer");
    // Static list of on demand schematics to test timeouts on
    private static final List<OnDemandSchematic> ON_DEMAND_SCHEMATICS = new ArrayList<>();

    // The last time this schematic was accessed. Can be null meaning this schematic is not currently cached
    private Long lastTimeAccessed = null;

    // The resource location to load schematic data from when needed
    private final ResourceLocation schematicLocation;

    // The name of the schematic
    private final String name;
    // A list of tile entities inside the schematic, starts null to avoid load overhead
    private NBTTagList tileEntities = null;
    // The width of the schematic
    private final short width;
    // The height of the schematic
    private final short height;
    // The length of the schematic
    private final short length;
    // The block IDs inside the schematic, starts null to avoid load overhead
    private Block[] blocks = null;
    // The raw byte data for each block, starts null to avoid load overhead
    private int[] data = null;
    // A list of entities inside the schematic, starts null to avoid load overhead
    private NBTTagList entities = null;

    static
    {
        TIMEOUT_TIMER.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                synchronized (ON_DEMAND_SCHEMATICS)
                {
                    ON_DEMAND_SCHEMATICS.forEach(OnDemandSchematic::checkTimeout);
                }
            }
        }, 0, 5000);
    }

    /**
     * Constructor initializes all fields that are not loaded on demand and are always cached
     *
     * @param schematicLocation The location to load schematic data from on demand
     * @param name              The name of the schematic
     * @param width             The width of the schematic
     * @param height            The height of the schematic
     * @param length            The length of the schematic
     */
    OnDemandSchematic(ResourceLocation schematicLocation, String name, short width, short height, short length)
    {
        this.schematicLocation = schematicLocation;
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;

        synchronized (ON_DEMAND_SCHEMATICS)
        {
            ON_DEMAND_SCHEMATICS.add(this);
        }
    }

    /**
     * Demands the block data to be cached and ready to go for generation
     */
    private synchronized void demandCache()
    {
        boolean wasTimedOut = this.isTimedOut();

        this.lastTimeAccessed = System.currentTimeMillis();
        if (wasTimedOut)
        {
            try
            {
                // Grab an input stream to the schematic file
                InputStream inputStream = ResourceUtil.getInputStream(this.schematicLocation);
                // Read the NBT data from the file
                NBTTagCompound nbtData = CompressedStreamTools.readCompressed(inputStream);

                // Read the entities and tile entities
                this.tileEntities = nbtData.getTagList("TileEntities", 10);
                this.entities = nbtData.getTagList("Entities", 10);

                // Read the blocks and data, use type 8 for string data.
                NBTTagList stringBlocks = nbtData.getTagList("Blocks", 8);
                this.data = nbtData.getIntArray("Data");

                // Convert all of our string blocks in the format of 'modid:registryname' to block pointer
                this.blocks = new Block[stringBlocks.tagCount()];
                for (int i = 0; i < blocks.length; i++)
                {
                    blocks[i] = Block.getBlockFromName(stringBlocks.getStringTagAt(i));
                    if (blocks[i] == null)
                    {
                        AfraidOfTheDark.INSTANCE.getLogger().error("Invalid schematic block found: " + stringBlocks.getStringTagAt(i));
                    }
                }

                AfraidOfTheDark.INSTANCE.getLogger().info("Loaded " + this.name + " into memory.");
            } catch (IOException e)
            {
                AfraidOfTheDark.INSTANCE.getLogger().error("Could not load on-demand schematic " + this.name, e);
            }
        }
    }

    /**
     * Checks the timeout of the schematic and clears the data if it's past the timeout
     */
    private synchronized void checkTimeout()
    {
        if (this.lastTimeAccessed != null && this.isTimedOut())
        {
            // Timeout, free memory
            this.lastTimeAccessed = null;
            this.tileEntities = null;
            this.blocks = null;
            this.data = null;
            this.entities = null;

            AfraidOfTheDark.INSTANCE.getLogger().info("Cleared " + this.name + " from memory.");
        }
    }

    /**
     * @return True if the schematic is timed out and can be removed from ram
     */
    private synchronized boolean isTimedOut()
    {
        if (this.lastTimeAccessed == null)
        {
            return true;
        }
        return (System.currentTimeMillis() - this.lastTimeAccessed) > AfraidOfTheDark.INSTANCE.getConfigurationHandler().getCacheTimeout();
    }

    /**
     * @return The name of the schematic
     */
    @Override
    public synchronized String getName()
    {
        return this.name;
    }

    /**
     * @return A list of tile entities in the schematic region
     */
    @Override
    public synchronized NBTTagList getTileEntities()
    {
        this.demandCache();
        return this.tileEntities;
    }

    /**
     * @return The width of the schematic region
     */
    @Override
    public short getWidth()
    {
        return this.width;
    }

    /**
     * @return The height of the schematic region
     */
    @Override
    public short getHeight()
    {
        return this.height;
    }

    /**
     * @return The length of the schematic region
     */
    @Override
    public short getLength()
    {
        return this.length;
    }

    /**
     * @return An array of blocks in the structure
     */
    @Override
    public synchronized Block[] getBlocks()
    {
        this.demandCache();
        return this.blocks;
    }

    /**
     * @return An array of block metadata values in the schematic
     */
    @Override
    public synchronized int[] getData()
    {
        this.demandCache();
        return this.data;
    }

    /**
     * @return A list of entities in the schematic region
     */
    @Override
    public synchronized NBTTagList getEntities()
    {
        this.demandCache();
        return this.entities;
    }
}
