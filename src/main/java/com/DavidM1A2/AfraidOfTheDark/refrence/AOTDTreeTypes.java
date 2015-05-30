/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.refrence;

import net.minecraft.util.IStringSerializable;

// All AOTD tree types are listed here
public enum AOTDTreeTypes implements IStringSerializable
{
	GRAVEWOOD;

	private static AOTDTreeTypes[] aotdTreeTypes = new AOTDTreeTypes[AOTDTreeTypes.values().length];

	public Integer getMetadata()
	{
		if (this == GRAVEWOOD)
		{
			return 0;
		}
		return null;
	}

	public static AOTDTreeTypes getTypeFromMeta(int meta)
	{
		if ((meta < 0) || (meta > AOTDTreeTypes.aotdTreeTypes.length))
		{
			meta = 0;
		}

		return AOTDTreeTypes.aotdTreeTypes[meta];
	}

	@Override
	public String toString()
	{
		if (this == GRAVEWOOD)
		{
			return "gravewood";
		}
		return null;
	}

	@Override
	public String getName()
	{
		return this.toString();
	}

	static
	{
		final AOTDTreeTypes[] var0 = AOTDTreeTypes.values();
		final int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2)
		{
			final AOTDTreeTypes var3 = var0[var2];
			AOTDTreeTypes.aotdTreeTypes[var3.getMetadata()] = var3;
		}
	}
}
