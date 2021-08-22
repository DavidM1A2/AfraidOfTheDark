package com.davidm1a2.afraidofthedark.common.registry.research

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation

class AOTDResearch(
    name: String,
    xPosition: Int,
    zPosition: Int,
    researchedRecipes: List<Item>,
    preResearchedRecipes: List<Item>,
    icon: ResourceLocation,
    preRequisiteId: ResourceLocation?
) : Research(xPosition, zPosition, researchedRecipes, preResearchedRecipes, icon, preRequisiteId) {
    init {
        // Set the registry name of the research to be 'modid:name'
        registryName = ResourceLocation("${Constants.MOD_ID}:$name")
    }
}