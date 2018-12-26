/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls;

import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

public class ItemResearchScrollCloakOfAgility extends ItemResearchScroll
{
	public ItemResearchScrollCloakOfAgility()
	{
		super();
		this.setUnlocalizedName("researchScrollCloakOfAgility");
	}

	@Override
	public void setMyType()
	{
		this.myType = ResearchTypes.CloakOfAgility;
	}
}
