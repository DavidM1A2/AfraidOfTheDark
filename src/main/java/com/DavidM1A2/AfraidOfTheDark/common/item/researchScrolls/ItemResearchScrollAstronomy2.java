/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls;

import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

public class ItemResearchScrollAstronomy2 extends ItemResearchScroll
{
	public ItemResearchScrollAstronomy2()
	{
		super();
		this.setUnlocalizedName("research_scroll_astronomy2");
		this.setRegistryName("research_scroll_astronomy2");
	}

	@Override
	public void setMyType()
	{
		this.myType = ResearchTypes.AstronomyII;
	}

	@Override
	public int numberOfScrollsToMakeCompleteResearch()
	{
		return 4;
	}
}
