/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

public enum AOTDDungeonTypes
{
	Crypt(23, "Crypt"),
	DarkForest(80, "Dark Forest"),
	GnomishCity(100, "Gnomish City"),
	Spring(20, "Spring"),
	VoidChest(10, "Void Chest"),
	WitchHut(8, "Witch Hut");

	private final int radius;
	private final String name;

	private AOTDDungeonTypes(int radius, String properName)
	{
		this.radius = radius;
		this.name = properName;
	}

	public int getRadius()
	{
		return this.radius;
	}

	public String getName()
	{
		return this.name;
	}

	public static AOTDDungeonTypes getDungeonFromName(String name)
	{
		for (AOTDDungeonTypes dungeon : AOTDDungeonTypes.values())
			if (dungeon.getName().equalsIgnoreCase(name))
				return dungeon;
		return null;
	}

	public static AOTDDungeonTypes getDungeonFromRadius(int radius)
	{
		for (AOTDDungeonTypes dungeon : AOTDDungeonTypes.values())
			if (dungeon.getRadius() == radius)
				return dungeon;
		return null;
	}
}
