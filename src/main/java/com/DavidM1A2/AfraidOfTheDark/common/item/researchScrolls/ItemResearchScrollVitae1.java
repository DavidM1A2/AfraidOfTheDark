package com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class ItemResearchScrollVitae1 extends ItemResearchScroll
{
	public ItemResearchScrollVitae1()
	{
		super();
		this.setUnlocalizedName("researchScrollVitae1");
	}

	@Override
	public void setMyType()
	{
		this.myType = ResearchTypes.VitaeI;
	}
}
