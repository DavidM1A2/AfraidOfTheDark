package com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class ItemResearchScrollWristCrossbow extends ItemResearchScroll
{
	public ItemResearchScrollWristCrossbow()
	{
		super();
		this.setUnlocalizedName("researchScrollWristCrossbow");
	}

	@Override
	public void setMyType()
	{
		this.myType = ResearchTypes.WristCrossbow;
	}
}
