/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */

package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.block.BlockMobSpawner;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SchematicLoader
{
	public static Schematic load(String schemname)
	{
		try
		{
			InputStream schematicInputStream = Utility.getInputStreamFromPath("assets/afraidofthedark/schematics/" + schemname);
			NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(schematicInputStream);
			schematicInputStream.close();
			short width = nbtdata.getShort("Width");
			short height = nbtdata.getShort("Height");
			short length = nbtdata.getShort("Length");

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

			NBTTagList tileEntities = nbtdata.getTagList("TileEntities", 10);
			NBTTagList entities = nbtdata.getTagList("Entities", 10);

			Schematic toReturn = new Schematic(tileEntities, width, height, length,blocks, data, entities);

			//SchematicBlockReplacer.fixKnownSchematicErrors(toReturn);

			if (ConfigurationHandler.debugMessages)
			{
				SchematicLoader.printIncorrectIds(toReturn.getBlocks(), schemname);
			}

			return toReturn;
		}
		catch (IOException e)
		{
			System.out.println("I can't load schematic: " + schemname + ", because " + e.toString() + "\nMessage: " + e.getMessage());
			return null;
		}
	}

	private static short[] byteArrayToShortArray(byte[] byteArray)
	{
		short[] toReturn = new short[byteArray.length];

		for (int i = 0; i < byteArray.length; i++)
		{
			toReturn[i] = byteArray[i];
		}

		return toReturn;
	}

	private static void printIncorrectIds(short[] blocks, String schematicName)
	{
		List<Short> incorrectIds = new ArrayList<Short>();
		int[] numberOfIncorrect = new int[20000];
		for (short b : blocks)
		{
			if (b < 0 && !incorrectIds.contains(b))
			{
				incorrectIds.add(b);
				numberOfIncorrect[Math.abs(b)] = 1;
			}
			else if (b < 0)
			{
				numberOfIncorrect[Math.abs(b)] = numberOfIncorrect[Math.abs(b)] + 1;
			}
		}
		for (short b : incorrectIds)
		{
			LogHelper.info(numberOfIncorrect[Math.abs(b)] + " incorrect ids of the id " + b + " found in the schematic " + schematicName + ".");
		}
	}
}
