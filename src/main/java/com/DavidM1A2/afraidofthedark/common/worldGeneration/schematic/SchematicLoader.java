package com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.utility.ResourceUtil;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;

/**
 * Class used to load schematic files from disk
 */
public class SchematicLoader
{
    /**
     * Loads a schematic from file into memory
     *
     * @param location The location of the schematic file
     * @return The loaded schematic
     */
    public static Schematic load(ResourceLocation location)
    {
        Schematic toReturn = null;
        try
        {
            // Grab an input stream to the schematic file
            InputStream inputStream = ResourceUtil.getInputStream(location);
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
            toReturn = new Schematic(tileEntities, width, height, length, blocks, data, entities);
        } catch (IOException e)
        {
            // Log an error
            AfraidOfTheDark.INSTANCE.getLogger().error("Could load the schematic " + location.getResourcePath() + ", error was:\n" + ExceptionUtils.getStackTrace(e));
        }

        // Error, return null
        return toReturn;
    }

    /**
     * Debug method used to write a schematic to disk
     *
     * @param schematic The schematic to write out
     * @param file      The file to write to
     */
    public static void writeToFile(Schematic schematic, File file)
    {
        // Don't overwrite files
        if (file.exists())
        {
            AfraidOfTheDark.INSTANCE.getLogger().warn("File already exists, returning...");
            return;
        }

        try
        {
            // Create folders down to the file
            file.getParentFile().mkdirs();
            // Create the file
            file.createNewFile();

            // Open an output stream to the file
            try (OutputStream outputStream = new FileOutputStream(file))
            {
                // Create a schematic NBT
                NBTTagCompound schematicNBT = new NBTTagCompound();

                // Write each of the w/l/h values to nbt
                schematicNBT.setShort("Width", schematic.getWidth());
                schematicNBT.setShort("Height", schematic.getHeight());
                schematicNBT.setShort("Length", schematic.getLength());

                // Write each entity and tile entity to the nbt
                schematicNBT.setTag("TileEntities", schematic.getTileEntities());
                schematicNBT.setTag("Entities", schematic.getEntities());

                // For each block write its name to nbt
                NBTTagList stringBlocks = new NBTTagList();
                for (Block block : schematic.getBlocks())
                {
                    stringBlocks.appendTag(new NBTTagString(block.getRegistryName().toString()));
                }
                schematicNBT.setTag("Blocks", stringBlocks);
                // Write all of the nbt data to disk
                schematicNBT.setIntArray("Data", schematic.getData());

                // Write the nbt to the file
                CompressedStreamTools.writeCompressed(schematicNBT, outputStream);
            }
        }
        // Catch the exception and print it out
        catch (IOException e)
        {
            AfraidOfTheDark.INSTANCE.getLogger().error(e);
        }
    }
}
