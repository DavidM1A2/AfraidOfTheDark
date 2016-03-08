package com.DavidM1A2.AfraidOfTheDark.common.refrence;

public enum AOTDDimensions
{
	Nightmare("nightmare", 67), VoidChest("voidChest", 68);

	private String worldName;
	private int worldID;
	private static final int BLOCKS_BETWEEN_ISLANDS = 992;

	private AOTDDimensions(String worldName, int worldID)
	{
		this.worldName = worldName;
		this.worldID = worldID;
	}

	public String getWorldName()
	{
		return this.worldName;
	}

	public int getWorldID()
	{
		return this.worldID;
	}

	public static int getBlocksBetweenIslands()
	{
		return BLOCKS_BETWEEN_ISLANDS;
	}
}
