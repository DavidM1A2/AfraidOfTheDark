package com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.utility.ResourceUtil;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Builds a schematic from a file on disk
 */
public class SchematicBuilder
{
    // True if the schematic should be cached in memory, false if it should be on demand
    private boolean cacheEnabled;
    // The file location of the schematic
    private ResourceLocation resourceLocation;

    /**
     * Creates a new schematic builder with the default cache enabled
     */
    public SchematicBuilder()
    {
        this.cacheEnabled = true;
        this.resourceLocation = null;
    }

    /**
     * Sets the cache enabled flag, true means the schematic data will be stored in memory, false means it will be
     * dynamically loaded when needed
     *
     * @param cacheEnabled True if the cache is enabled false otherwise
     * @return The builder instance
     */
    public SchematicBuilder withCacheEnabled(boolean cacheEnabled)
    {
        this.cacheEnabled = cacheEnabled;
        return this;
    }

    /**
     * Sets the file to read the schematic data from
     *
     * @param resourceLocation The schematic resource location
     * @return The builder instance
     */
    public SchematicBuilder withFile(ResourceLocation resourceLocation)
    {
        this.resourceLocation = resourceLocation;
        return this;
    }

    /**
     * Builds the schematic instance, the resource location must be set
     *
     * @return The schematic ready to be generated in the world
     */
    public Schematic build()
    {
        if (this.resourceLocation == null)
        {
            throw new IllegalArgumentException("Resource location must be specified!");
        }

        try
        {
            if (this.cacheEnabled)
            {
                return this.createCached();
            }
            else
            {
                return this.createOnDemand();
            }
        } catch (IOException e)
        {
            // Log an error
            AfraidOfTheDark.INSTANCE.getLogger().error("Could load the schematic " + this.resourceLocation.getResourcePath() + ", error was:\n" + ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * Creates a cached schematic that stores all of its data in memory
     *
     * @return A cached schematic instance
     * @throws IOException If the schematic file does not exist or can't be read
     */
    private Schematic createCached() throws IOException
    {
        // Grab an input stream to the schematic file
        InputStream inputStream = ResourceUtil.getInputStream(this.resourceLocation);
        // Read the NBT data from the file
        NBTTagCompound nbtData = CompressedStreamTools.readCompressed(inputStream);
        // Close the input stream
        inputStream.close();

        // Begin processing the data
        short width = nbtData.getShort("Width");
        short height = nbtData.getShort("Height");
        short length = nbtData.getShort("Length");

        // Read the entities and tile entities
        NBTTagList tileEntities = nbtData.getTagList("TileEntities", 10);
        NBTTagList entities = nbtData.getTagList("Entities", 10);

        // Read the blocks and data, use type 8 for string data.
        NBTTagList stringBlocks = nbtData.getTagList("Blocks", 8);
        int[] data = nbtData.getIntArray("Data");

        // Convert all of our string blocks in the format of 'modid:registryname' to block pointer
        Block[] blocks = new Block[stringBlocks.tagCount()];
        for (int i = 0; i < blocks.length; i++)
        {
            blocks[i] = Block.getBlockFromName(stringBlocks.getStringTagAt(i));
            if (blocks[i] == null)
            {
                AfraidOfTheDark.INSTANCE.getLogger().error("Invalid schematic block found: " + stringBlocks.getStringTagAt(i));
            }
        }

        // Return the schematic
        return new CachedSchematic(tileEntities, width, height, length, blocks, data, entities);
    }

    /**
     * Creates an on demand schematic that dynamically loads its block data when needed
     *
     * @return An on demand schematic instance
     * @throws IOException If the schematic file does not exist or can't be read
     */
    private Schematic createOnDemand() throws IOException
    {
        return new OnDemandSchematic();
    }
}
