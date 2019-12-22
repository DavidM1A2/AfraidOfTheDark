package com.davidm1a2.afraidofthedark.common.registry.research

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.util.ResourceLocation

/**
 * Base class for all researches created for AOTD
 *
 * @constructor takes the name of the JSON file which contains the research data in it as the first parameter and
 * the pre-requisite for this research as the second argument
 * @param name         The name of the research found in afraidofthedark:research_notes/<name>.json
 * @param preRequisite The prerequisite research to this one
 */
class AOTDResearch(name: String, preRequisite: Research?) : Research(ResourceLocation(Constants.MOD_ID, "research_notes/$name.json"), preRequisite)
{
    init
    {
        // Set the registry name of the research to be 'modid:name'
        registryName = ResourceLocation("${Constants.MOD_ID}:$name")
    }
}