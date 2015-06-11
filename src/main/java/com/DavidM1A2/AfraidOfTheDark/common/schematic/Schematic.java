/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */
package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import net.minecraft.nbt.NBTTagList;

public class Schematic
{
	private final NBTTagList tileentities;
	private final short width;
	private final short height;
	private final short length;
	private final byte[] blocks;
	private final byte[] data;

	public Schematic(NBTTagList tileentities, short width, short height, short length, byte[] blocks, byte[] data)
	{
		this.tileentities = tileentities;
		this.width = width;
		this.height = height;
		this.length = length;
		this.blocks = blocks;
		this.data = data;
	}

	public NBTTagList getTileentities() 
	{
		return tileentities;
	}

	public short getWidth() 
	{ 
		return width;
	}

	public short getHeight() 
	{
		return height;
	}

	public short getLength() 
	{
		return length;
	}

	public byte[] getBlocks() 
	{
		return blocks;
	}

	public byte[] getData() 
	{
		return data;
	}
}
