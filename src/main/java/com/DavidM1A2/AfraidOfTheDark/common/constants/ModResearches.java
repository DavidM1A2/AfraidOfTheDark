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
	public static final Research CROSSBOW = new AOTDResearch("crossbow", AN_UNBREAKABLE_COVENANT);
	public static final Research WRIST_CROSSBOW = new AOTDResearch("wrist_crossbow", CROSSBOW);
	public static final Research WEREWOLF_EXAMINATION = new AOTDResearch("werewolf_examination", AN_UNBREAKABLE_COVENANT);
	public static final Research ASTRONOMY_1 = new AOTDResearch("astronomy_1", WEREWOLF_EXAMINATION);

	// An array containing a list of researches that AOTD adds
	public static Research[] RESEARCH_LIST = new Research[]
	{
		AN_UNBREAKABLE_COVENANT,
		ENCHANTED_SKELETON,
		BLADE_OF_EXHUMATION,
		CROSSBOW,
		WRIST_CROSSBOW,
		WEREWOLF_EXAMINATION,
		ASTRONOMY_1
	};
}
