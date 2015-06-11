package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import java.io.InputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SchematicLoader
{
	public Schematic get(String schemname)
	{
		try
		{
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/mymod/schem/" + schemname);
			NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(is);
			short width = nbtdata.getShort("Width");
			short height = nbtdata.getShort("Height");
			short length = nbtdata.getShort("Length");

			byte[] blocks = nbtdata.getByteArray("Blocks");
			byte[] data = nbtdata.getByteArray("Data");

			System.out.println("schem size:" + width + " x " + height + " x " + length);
			NBTTagList tileentities = nbtdata.getTagList("TileEntities", 10);
			is.close();

			return new Schematic(tileentities, width, height, length, blocks, data);
		}
		catch (Exception e)
		{
			System.out.println("I can't load schematic, because " + e.toString());
			return null;
		}
	}
}
