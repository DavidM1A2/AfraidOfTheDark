package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.research.base.AOTDResearch;
import com.DavidM1A2.afraidofthedark.common.research.base.Research;

/**
 * A static class containing all of our research references for us
 */
public class ModResearches
{
	public static final Research AN_UNBREAKABLE_COVENANT = new AOTDResearch("an_unbreakable_covenant", null);
	public static final Research ENCHANTED_SKELETON = new AOTDResearch("enchanted_skeleton", AN_UNBREAKABLE_COVENANT);
	public static final Research BLADE_OF_EXHUMATION = new AOTDResearch("blade_of_exhumation", ENCHANTED_SKELETON);

	// An array containing a list of researches that AOTD adds
	public static Research[] RESEARCH_LIST = new Research[]
	{
		AN_UNBREAKABLE_COVENANT,
		ENCHANTED_SKELETON,
		BLADE_OF_EXHUMATION
	};
}
