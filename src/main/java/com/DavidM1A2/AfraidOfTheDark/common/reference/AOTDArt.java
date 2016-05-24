/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.reference;

import net.minecraft.util.ResourceLocation;

public enum AOTDArt
{
	Victorian1("Victorian1.png", 256, 256),
	Victorian2("Victorian2.png", 256, 256);

	private final ResourceLocation texture;
	private final int width;
	private final int height;

	private AOTDArt(String texture, int width, int height)
	{
		this.texture = new ResourceLocation("afraidofthedark:textures/painting/" + texture);
		this.width = width;
		this.height = height;
	}

	public int getWidthPixels()
	{
		return this.width;
	}

	public int getHeightPixels()
	{
		return this.height;
	}

	public ResourceLocation getTexture()
	{
		return this.texture;
	}

	public String getName()
	{
		return this.name();
	}

	public static AOTDArt fromName(String name)
	{
		for (AOTDArt art : AOTDArt.values())
			if (art.name().equals(name))
				return art;
		return null;
	}

	public String formattedString()
	{
		String toReturn = "";

		for (final String string : this.toString().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
		{
			toReturn = toReturn + string + " ";
		}

		return toReturn;
	}
}
