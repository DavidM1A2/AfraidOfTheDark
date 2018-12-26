/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.potion;

import net.minecraft.util.ResourceLocation;

public class SleepingPotion extends AOTDPotion
{
	public SleepingPotion(int id, ResourceLocation resourceLocation, boolean isBad, int color)
	{
		super(id, resourceLocation, isBad, color);
		this.setPotionName("Drowsyness");
	}
}
