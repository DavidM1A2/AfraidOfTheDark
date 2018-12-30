package com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import org.codehaus.plexus.util.ExceptionUtils;

import java.io.IOException;
import java.io.InputStream;

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
		try
		{
			// Grab an input stream to the schematic file
			InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
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
				blocks[i] = Block.getBlockFromName(stringBlocks.getStringTagAt(i));

			// Return the schematic
			return new Schematic(tileEntities, width, height, length, blocks, data, entities);
		}
		catch (IOException e)
		{
			// Log an error
			AfraidOfTheDark.INSTANCE.getLogger().error("Could load the schematic " + location.getResourcePath() + ", error was:\n" + ExceptionUtils.getStackTrace(e));
		}

		// Error, return null
		return null;
	}
}
