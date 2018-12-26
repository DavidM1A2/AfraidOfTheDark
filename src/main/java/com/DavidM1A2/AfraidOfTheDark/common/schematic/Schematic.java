/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */
package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import com.mojang.realmsclient.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagList;

public class Schematic
{
	private final NBTTagList tileEntities;
	private final short width;
	private final short height;
	private final short length;
	private final short[] blocks;
	private final byte[] data;
	private final NBTTagList entities;

	public Schematic(NBTTagList tileEntities, short width, short height, short length, short[] blocks, byte[] data, NBTTagList entities)
	{
		this.tileEntities = tileEntities;
		this.width = width;
		this.height = height;
		this.length = length;
		this.blocks = blocks;
		this.data = data;
		this.entities = entities;
	}

	public NBTTagList getTileEntities()
	{
		return tileEntities;
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
		blocks[index] = new Integer(Block.getIdFromBlock(block)).shortValue();
	}

	public void setBlock(Short block, int index)
	{
		blocks[index] = block;
	}

	public void replaceBlocks(Pair<Block, Block>... blocks)
	{
		for (Pair<Block, Block> pair : blocks)
			this.replaceBlock(pair);
	}

	public void replaceBlock(Pair<Block, Block> blocks)
	{
		for (int i = 0; i < this.getBlocks().length; i++)
			if (Block.getBlockById(this.getBlocks()[i]) == blocks.first())
				this.setBlock(blocks.second(), i);
	}
}
