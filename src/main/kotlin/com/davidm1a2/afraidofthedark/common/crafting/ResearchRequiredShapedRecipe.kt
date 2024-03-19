package com.davidm1a2.afraidofthedark.common.crafting

import com.davidm1a2.afraidofthedark.common.constants.ModRecipeSerializers
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.common.crafting.IShapedRecipe

/**
 * Class representing a shaped recipe that requires a research before it can be crafted
 *
 * @constructor just takes a recipe as base and a pre-requisite recipe to research
 * @param baseRecipe   The base recipe to start with
 * @param preRequisite The pre-requisite research to be required to craft this recipe
 */
class ResearchRequiredShapedRecipe(baseRecipe: IShapedRecipe<CraftingContainer>, preRequisite: Research) :
    ResearchRequiredRecipeBase<IShapedRecipe<CraftingContainer>>(baseRecipe, preRequisite), IShapedRecipe<CraftingContainer> {
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

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.RESEARCH_REQUIRED_SHAPED_RECIPE
    }
}