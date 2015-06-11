package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import net.minecraft.nbt.NBTTagList;

public class Schematic
{
	public NBTTagList tileentities;
	public short width;
	public short height;
	public short length;
	public byte[] blocks;
	public byte[] data;

	public Schematic(NBTTagList tileentities, short width, short height, short length, byte[] blocks, byte[] data)
	{
		this.tileentities = tileentities;
		this.width = width;
		this.height = height;
		this.length = length;
		this.blocks = blocks;
		this.data = data;
	}
}
