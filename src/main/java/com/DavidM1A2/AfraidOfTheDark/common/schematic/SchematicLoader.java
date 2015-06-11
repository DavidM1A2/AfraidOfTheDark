/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */

package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import java.io.InputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SchematicLoader
{
	public static Schematic load(String schemname)
	{
		try
		{
			InputStream schematicInputStream = SchematicLoader.class.getClassLoader().getResourceAsStream("assets/afraidofthedark/schematics/" + schemname);
			NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(schematicInputStream);
			short width = nbtdata.getShort("Width");
			short height = nbtdata.getShort("Height");
			short length = nbtdata.getShort("Length");

			byte[] blocks = nbtdata.getByteArray("Blocks");
			byte[] data = nbtdata.getByteArray("Data");

			NBTTagList tileentities = nbtdata.getTagList("TileEntities", 10);

			schematicInputStream.close();

			return new Schematic(tileentities, width, height, length, byteArrayToShortArray(blocks), data);
		}
		catch (Exception e)
		{
			System.out.println("I can't load schematic, because " + e.toString());
			return null;
		}
	}

	private static short[] byteArrayToShortArray(byte[] byteArray)
	{
		short[] toReturn = new short[byteArray.length];

		for (int i = 0; i < byteArray.length; i++)
		{
			toReturn[i] = (short) byteArray[i];
		}

		return toReturn;
	}
}
