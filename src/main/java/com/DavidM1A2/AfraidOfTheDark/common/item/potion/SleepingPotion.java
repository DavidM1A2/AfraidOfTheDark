/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.potion;

import net.minecraft.util.ResourceLocation;

public class SleepingPotion extends AOTDPotion
{
	public SleepingPotion(ResourceLocation resourceLocation, boolean isBad, int color)
	{
		super(resourceLocation, isBad, color);
		this.setPotionName("drowsyness");
	}
}
