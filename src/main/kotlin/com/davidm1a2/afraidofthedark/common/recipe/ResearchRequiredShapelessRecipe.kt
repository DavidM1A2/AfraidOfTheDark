package com.davidm1a2.afraidofthedark.common.recipe

import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.item.crafting.IRecipe

/**
 * Class representing a shapeless recipe that requires a research before it can be crafted
 *
 * @constructor just takes a shapeless recipe as base and a pre-requisite recipe to research
 * @param baseRecipe   The base recipe to start with
 * @param preRequisite The pre-requisite research to be required to craft this recipe
 */
class ResearchRequiredShapelessRecipe(baseRecipe: IRecipe, preRequisite: Research) :
    ResearchRequiredRecipeBase<IRecipe>(baseRecipe, preRequisite)
