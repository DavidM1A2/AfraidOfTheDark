package com.DavidM1A2.afraidofthedark.common.registry.research;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.util.ResourceLocation;

/**
 * Base class for all researches created for AOTD
 */
public class AOTDResearch extends Research
{
	/**
	 * Constructor takes the name of the JSON file which contains the research data in it as the first parameter and
	 * the pre-requisite for this research as the second argument
	 *
	 * @param name The name of the research found in afraidofthedark:research_notes/<name>.json
	 * @param preRequisite The prerequisite research to this one
	 */
	public AOTDResearch(String name, Research preRequisite)
	{
		super(new ResourceLocation(Constants.MOD_ID, "research_notes/" + name + ".json"), preRequisite);
		// Set the registry name of the research to be 'modid:name'
		this.setRegistryName(new ResourceLocation(Constants.MOD_ID + ":" + name));
	}
}
