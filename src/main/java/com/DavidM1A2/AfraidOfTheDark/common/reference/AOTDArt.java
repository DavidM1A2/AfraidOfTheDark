/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.reference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum AOTDArt
{
	Victorian1("victorian_1.png", 256, 256, 12),
	Victorian2("victorian_2.png", 256, 256, 12);

	private final ResourceLocation texture;

	private final int width;
	private final int height;
	private final int blockScale;

	private AOTDArt(String texture, int width, int height, int blockScale)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			this.texture = new ResourceLocation("afraidofthedark:textures/painting/" + texture);
		else
			this.texture = null;
		this.width = width;
		this.height = height;
		this.blockScale = blockScale;
	}

	public int getWidthPixels()
	{
		return this.width;
	}

	public int getHeightPixels()
	{
		return this.height;
	}

	public int getBlockScale()
	{
		return this.blockScale;
	}

	@SideOnly(Side.CLIENT)
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
