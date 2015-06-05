package com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class ItemResearchScrollAstronomy2 extends ItemResearchScroll
{
	public ItemResearchScrollAstronomy2()
	{
		super();
		this.setUnlocalizedName("researchScrollAstronomy2");
	}

	@Override
	public void setMyType()
	{
		this.myType = ResearchTypes.AstronomyII;
	}
}
