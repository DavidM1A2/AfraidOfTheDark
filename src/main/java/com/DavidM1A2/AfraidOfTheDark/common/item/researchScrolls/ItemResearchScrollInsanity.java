/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class ItemResearchScrollInsanity extends ItemResearchScroll
{
	public ItemResearchScrollInsanity()
	{
		super();
		this.setUnlocalizedName("researchScrollInsanity");
	}

	@Override
	public void setMyType()
	{
		this.myType = ResearchTypes.Insanity;
	}
}
