/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */
package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagList;

public class Schematic
{
	private final NBTTagList tileentities;
	private final short width;
	private final short height;
	private final short length;
	private final short[] blocks;
	private final byte[] data;
	private final NBTTagList entities;

	public Schematic(NBTTagList tileentities, short width, short height, short length, short[] blocks, byte[] data, NBTTagList entities)
	{
		this.tileentities = tileentities;
		this.width = width;
		this.height = height;
		this.length = length;
		this.blocks = blocks;
		this.data = data;
		this.entities = entities;
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

	public short[] getBlocks()
	{
		return blocks;
	}

	public byte[] getData()
	{
		return data;
	}

	public NBTTagList getEntities()
	{
		return this.entities;
	}

	public void setBlock(Block block, int index)
	{
		blocks[index] = Short.parseShort(Integer.toString(Block.getIdFromBlock(block)));
	}

	public void setBlock(Short block, int index)
	{
		blocks[index] = block;
	}
}
