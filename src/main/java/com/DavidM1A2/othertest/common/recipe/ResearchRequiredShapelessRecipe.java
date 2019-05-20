package com.DavidM1A2.afraidofthedark.common.recipe;

import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import net.minecraft.item.crafting.IRecipe;

/**
 * Class representing a shapeless recipe that requires a research before it can be crafted
 */
public class ResearchRequiredShapelessRecipe extends ResearchRequiredRecipeBase<IRecipe>
{
    /**
     * Constructor just takes a shapeless recipe as base and a pre-requisite recipe to research
     *
     * @param baseRecipe   The base recipe to start with
     * @param preRequisite The pre-requisite research to be required to craft this recipe
     */
    public ResearchRequiredShapelessRecipe(IRecipe baseRecipe, Research preRequisite)
    {
        super(baseRecipe, preRequisite);
    }
}
