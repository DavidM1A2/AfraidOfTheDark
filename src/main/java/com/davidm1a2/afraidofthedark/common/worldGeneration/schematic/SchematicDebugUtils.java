package com.davidm1a2.afraidofthedark.common.worldGeneration.schematic;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.constants.Constants;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Collection of utility methods used to debug, not used in actual play
 */
public class SchematicDebugUtils
{
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

    /**
     * Debug function to create or update existing schematic meta files
     */
    public static void createSchematicMetaFiles()
    {
        generateMcMetaFileForDir(new File("../src/main/resources/assets/afraidofthedark/schematics"));
    }

    /**
     * Generates all the .schematic.mcmeta files for this directory
     *
     * @param schematicDir The directory to generate files in
     */
    private static void generateMcMetaFileForDir(File schematicDir)
    {
        // Lists all files the directory
        File[] subfiles = schematicDir.listFiles();
        if (subfiles != null)
        {
            // Go over each subfile, the ones that are directories we recurse over, the ones that are schematic files we create a meta file for
            Arrays.stream(subfiles).filter(File::isDirectory).forEach(SchematicDebugUtils::generateMcMetaFileForDir);
            Arrays.stream(subfiles).filter(File::isFile).filter(file -> file.getName().endsWith(".schematic")).forEach(SchematicDebugUtils::createMetaFor);
        }
    }

    /**
     * Creates a .meta file for the schematic, if one already exists it's updated
     *
     * @param schematicFile The schematic file to create
     */
    private static void createMetaFor(File schematicFile)
    {
        // Create .meta file for the schematic
        File schematicMetaFile = new File(schematicFile.getAbsolutePath() + ".meta");
        // Delete the existing one
        if (schematicMetaFile.exists())
        {
            schematicMetaFile.delete();
        }

        // Get the path to the original schematic file and load it
        String localPath = StringUtils.substringAfter(schematicFile.getAbsolutePath(), "src\\main\\resources\\assets\\afraidofthedark\\");
        Schematic schematic = new SchematicBuilder().withFile(new ResourceLocation(Constants.MOD_ID, localPath)).withCacheEnabled(true).build();

        // Create an NBT compound to write to
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("width", schematic.getWidth());
        nbt.setShort("height", schematic.getHeight());
        nbt.setShort("length", schematic.getLength());

        // Write the NBT to the .meta file
        try (FileOutputStream fileOutputStream = new FileOutputStream(schematicMetaFile))
        {
            CompressedStreamTools.writeCompressed(nbt, fileOutputStream);
        } catch (IOException e)
        {
            System.err.println("Could not write schematic .meta file:\n" + ExceptionUtils.getStackTrace(e));
        }
    }
}
