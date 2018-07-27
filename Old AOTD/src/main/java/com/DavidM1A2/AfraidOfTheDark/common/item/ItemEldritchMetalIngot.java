/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;

public class ItemEldritchMetalIngot extends AOTDItem
{
	// Quick silver ingot item
	public ItemEldritchMetalIngot()
	{
		super();
		this.setUnlocalizedName("eldritch_metal_ingot");
		this.setRegistryName("eldritch_metal_ingot");
		this.maxStackSize = 64;
	}
}
