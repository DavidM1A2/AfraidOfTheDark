/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */

package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.lang3.ArrayUtils;

public class SchematicLoader
{
	public static Schematic load(String schemname)
	{
		try
		{
			InputStream schematicInputStream = Utility.getInputStreamFromPath("assets/afraidofthedark/schematics/" + schemname);
			NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(schematicInputStream);
			schematicInputStream.close();
			// Read the length, width, and height
			short width = nbtdata.getShort("Width");
			short height = nbtdata.getShort("Height");
			short length = nbtdata.getShort("Length");

			// Read the blocks and metadata
			byte[] blocksByte = nbtdata.getByteArray("Blocks");
			short[] blocks = new short[blocksByte.length];
			byte[] data = nbtdata.getByteArray("Data");
			byte[] addBlocks = new byte[0];

			// see https://github.com/sk89q/WorldEdit/blob/master/worldedit-core/src/main/java/com/sk89q/worldedit/schematic/MCEditSchematicFormat.java
			// line 125. We save the 4 high level bits of the block into the AddBlocks array, so re-combine the byte & addBlocks array here into a
			// short array.

			// AddBlocks are the highest 4 bits in a block ID
			if (nbtdata.hasKey("AddBlocks"))
				addBlocks = nbtdata.getByteArray("AddBlocks");

			for (int index = 0; index < blocksByte.length; index++)
			{
				// No AddBlocks index
				if ((index >> 1) >= addBlocks.length)
				{
					blocks[index] = (short) (blocksByte[index] & 0xFF);
				}
				else
				{
					if ((index & 1) == 0)
					{
						blocks[index] = (short) (((addBlocks[index >> 1] & 0x0F) << 8) + (blocksByte[index] & 0xFF));
					}
					else
					{
						blocks[index] = (short) (((addBlocks[index >> 1] & 0xF0) << 4) + (blocksByte[index] & 0xFF));
					}
				}
			}

			// Read the entities and tile entities
			NBTTagList tileEntities = nbtdata.getTagList("TileEntities", 10);
			NBTTagList entities = nbtdata.getTagList("Entities", 10);

			// Create the schematic object
			Schematic toReturn = new Schematic(tileEntities, width, height, length,blocks, data, entities);

			// If we have an IDMap, we need to remap certain IDs in the schematic to the respective
            // IDs in the current MC instance
			if (nbtdata.hasKey("IDMap"))
            {
                NBTTagList replacements = nbtdata.getTagList("IDMap", 10);
                Map<Integer, Block> idToReal = new HashMap<Integer, Block>();
                for (int i = 0; i < replacements.tagCount(); i++)
                {
                    NBTTagCompound x = replacements.getCompoundTagAt(i);
                    String blockName = x.getString("K");
                    Integer blockID = x.getInteger("V");
                    idToReal.put(blockID, Block.getBlockFromName(blockName));
                }
                for (int j = 0; j < toReturn.getBlocks().length; j++)
                    if (idToReal.containsKey((int) toReturn.getBlocks()[j]))
                        toReturn.setBlock(idToReal.get((int) toReturn.getBlocks()[j]), j);
            }

			return toReturn;
		}
		catch (IOException e)
		{
			System.out.println("I can't load schematic: " + schemname + ", because " + e.toString() + "\nMessage: " + e.getMessage());
			return null;
		}
	}
}
