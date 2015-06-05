package com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

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
