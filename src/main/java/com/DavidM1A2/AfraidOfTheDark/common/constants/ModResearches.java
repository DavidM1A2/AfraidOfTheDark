package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.registry.research.AOTDResearch;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;

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
	public static final Research ASTRAL_SILVER = new AOTDResearch("astral_silver", ASTRONOMY_1);
	public static final Research SLAYING_OF_THE_WOLVES = new AOTDResearch("slaying_of_the_wolves", ASTRAL_SILVER);
	public static final Research PHYLACTERY_OF_SOULS = new AOTDResearch("phylactery_of_souls", SLAYING_OF_THE_WOLVES);
	public static final Research CLOAK_OF_AGILITY = new AOTDResearch("cloak_of_agility", WEREWOLF_EXAMINATION);
	public static final Research VOID_CHEST = new AOTDResearch("void_chest", ASTRAL_SILVER);
	public static final Research ELDRITCH_DECORATION = new AOTDResearch("eldritch_decoration", VOID_CHEST);

	// An array containing a list of researches that AOTD adds
	public static Research[] RESEARCH_LIST = new Research[]
	{
		AN_UNBREAKABLE_COVENANT,
		ENCHANTED_SKELETON,
		BLADE_OF_EXHUMATION,
		CROSSBOW,
		WRIST_CROSSBOW,
		WEREWOLF_EXAMINATION,
		ASTRONOMY_1,
		ASTRAL_SILVER,
		SLAYING_OF_THE_WOLVES,
		PHYLACTERY_OF_SOULS,
		CLOAK_OF_AGILITY,
		VOID_CHEST,
		ELDRITCH_DECORATION
	};
}
