package com.DavidM1A2.afraidofthedark.common.schematic;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagList;

/**
 * Object representation of a schematic file (.schematic)
 */
public class Schematic
{
	// A list of tile entities inside the schematic
	private final NBTTagList tileEntities;
	// The width of the schematic
	private final short width;
	// The height of the schematic
	private final short height;
	// The length of the schematic
	private final short length;
	// The block IDs inside the schematic
	private final Block[] blocks;
	// The raw byte data for each block
	private final int[] data;
	// A list of entities inside the schematic
	private final NBTTagList entities;

	/**
	 * Constructor just initializes fields
	 *
	 * @param tileEntities The tile entities (like chests, etc)
	 * @param width The width of the schematic
	 * @param height The height of the schematic
	 * @param length The length of the schematic
	 * @param blocks The block IDs of each block inside the schematic
	 * @param data The block data of each block inside the schematic
	 * @param entities The entities inside the schematic
	 */
	public Schematic(NBTTagList tileEntities, short width, short height, short length, Block[] blocks, int[] data, NBTTagList entities)
	{
		this.tileEntities = tileEntities;
		this.width = width;
		this.height = height;
		this.length = length;
		this.blocks = blocks;
		this.data = data;
		this.entities = entities;
	}

	///
	/// Getters for all the schematic data
	///

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

	public Block[] getBlocks()
	{
		return blocks;
	}

	public int[] getData()
	{
		return data;
	}

	public NBTTagList getEntities()
	{
		return this.entities;
	}
}
