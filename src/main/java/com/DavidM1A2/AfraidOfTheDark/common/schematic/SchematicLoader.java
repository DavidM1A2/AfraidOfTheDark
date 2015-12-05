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

			byte[] blocks = nbtdata.getByteArray("Blocks");

			byte[] data = nbtdata.getByteArray("Data");

			NBTTagList tileentities = nbtdata.getTagList("TileEntities", 10);
			NBTTagList entities = nbtdata.getTagList("Entities", 10);

			Schematic toReturn = new Schematic(tileentities, width, height, length, byteArrayToShortArray(blocks), data, entities);

			SchematicBlockReplacer.fixKnownSchematicErrors(toReturn);

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
