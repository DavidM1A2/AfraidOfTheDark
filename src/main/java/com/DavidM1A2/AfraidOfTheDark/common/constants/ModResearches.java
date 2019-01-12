package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.research.base.AOTDResearch;
import com.DavidM1A2.afraidofthedark.common.research.base.Research;

/**
 * A static class containing all of our research references for us
 */
public class ModResearches
{
	public static final Research AN_UNBREAKABLE_COVENANT = new AOTDResearch("an_unbreakable_covenant", null);

	// An array containing a list of researches that AOTD adds
	public static Research[] RESEARCH_LIST = new Research[]
	{
		AN_UNBREAKABLE_COVENANT
	};
}
