package com.DavidM1A2.AfraidOfTheDark.item.researchScrolls;

import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;

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
