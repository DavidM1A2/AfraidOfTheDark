package com.davidm1a2.afraidofthedark.common.recipe;

import com.davidm1a2.afraidofthedark.common.registry.research.Research;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Class representing a shaped recipe that requires a research before it can be crafted
 */
public class ResearchRequiredShapedRecipe extends ResearchRequiredRecipeBase<IShapedRecipe> implements IShapedRecipe
{
    /**
     * Constructor just takes a recipe as base and a pre-requisite recipe to research
     *
     * @param baseRecipe   The base recipe to start with
     * @param preRequisite The pre-requisite research to be required to craft this recipe
     */
    public ResearchRequiredShapedRecipe(IShapedRecipe baseRecipe, Research preRequisite)
    {
        super(baseRecipe, preRequisite);
    }

    /**
     * @return The width of the recipe
     */
    @Override
    public int getRecipeWidth()
    {
        return this.baseRecipe.getRecipeWidth();
    }

    /**
     * @return The height of the recipe
     */
    @Override
    public int getRecipeHeight()
    {
        return this.baseRecipe.getRecipeHeight();
    }
}
