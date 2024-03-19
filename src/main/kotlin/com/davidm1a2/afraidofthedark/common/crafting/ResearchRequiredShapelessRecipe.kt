package com.davidm1a2.afraidofthedark.common.crafting

import com.davidm1a2.afraidofthedark.common.constants.ModRecipeSerializers
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.RecipeSerializer

/**
 * Class representing a shapeless recipe that requires a research before it can be crafted
 *
 * @constructor just takes a shapeless recipe as base and a pre-requisite recipe to research
 * @param baseRecipe   The base recipe to start with
 * @param preRequisite The pre-requisite research to be required to craft this recipe
 */
class ResearchRequiredShapelessRecipe(baseRecipe: CraftingRecipe, preRequisite: Research) :
    ResearchRequiredRecipeBase<CraftingRecipe>(baseRecipe, preRequisite) {
    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.RESEARCH_REQUIRED_SHAPELESS_RECIPE
    }
}
