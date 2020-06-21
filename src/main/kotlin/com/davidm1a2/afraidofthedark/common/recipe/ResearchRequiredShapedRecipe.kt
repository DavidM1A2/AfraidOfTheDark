package com.davidm1a2.afraidofthedark.common.recipe

import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraftforge.common.crafting.IShapedRecipe

/**
 * Class representing a shaped recipe that requires a research before it can be crafted
 *
 * @constructor just takes a recipe as base and a pre-requisite recipe to research
 * @param baseRecipe   The base recipe to start with
 * @param preRequisite The pre-requisite research to be required to craft this recipe
 */
class ResearchRequiredShapedRecipe(baseRecipe: IShapedRecipe, preRequisite: Research) :
    ResearchRequiredRecipeBase<IShapedRecipe>(baseRecipe, preRequisite), IShapedRecipe {
    /**
     * @return The width of the recipe
     */
    override fun getRecipeWidth(): Int {
        return baseRecipe.recipeWidth
    }

    /**
     * @return The height of the recipe
     */
    override fun getRecipeHeight(): Int {
        return baseRecipe.recipeHeight
    }
}