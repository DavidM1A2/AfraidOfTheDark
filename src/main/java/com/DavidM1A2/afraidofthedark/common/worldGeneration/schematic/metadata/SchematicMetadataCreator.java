package com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.metadata;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.Schematic;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicLoader;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Utility class to create 'X.schematic.meta' files used in creating schematic caches
 */
public class SchematicMetadataCreator
{
    // The metadata has 4 fields, size (width, height, length) and name
    private static final String NBT_WIDTH = "width";
    private static final String NBT_HEIGHT = "height";
    private static final String NBT_LENGTH = "length";
    private static final String NBT_NAME = "name";

    /**
     * Creates or updates existing schematic meta files
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
            Arrays.stream(subfiles).filter(File::isDirectory).forEach(SchematicMetadataCreator::generateMcMetaFileForDir);
            Arrays.stream(subfiles).filter(File::isFile).filter(file -> file.getName().endsWith(".schematic")).forEach(SchematicMetadataCreator::createMetaFor);
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
        Schematic schematic = SchematicLoader.load(new ResourceLocation(Constants.MOD_ID, localPath));

        // Create an NBT compound to write to
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort(NBT_WIDTH, schematic.getWidth());
        nbt.setShort(NBT_HEIGHT, schematic.getHeight());
        nbt.setShort(NBT_LENGTH, schematic.getLength());
        nbt.setString(NBT_NAME, FilenameUtils.getBaseName(schematicFile.getName()));

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
