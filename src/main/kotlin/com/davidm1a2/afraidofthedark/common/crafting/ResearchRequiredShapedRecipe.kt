package com.davidm1a2.afraidofthedark.common.crafting

import com.davidm1a2.afraidofthedark.common.constants.ModRecipeSerializers
import com.davidm1a2.afraidofthedark.common.registry.Research
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraftforge.common.crafting.IShapedRecipe

/**
 * Class representing a shaped recipe that requires a research before it can be crafted
 *
 * @constructor just takes a recipe as base and a pre-requisite recipe to research
 * @param baseRecipe   The base recipe to start with
 * @param preRequisite The pre-requisite research to be required to craft this recipe
 */
class ResearchRequiredShapedRecipe(baseRecipe: IShapedRecipe<CraftingInventory>, preRequisite: Research) :
    ResearchRequiredRecipeBase<IShapedRecipe<CraftingInventory>>(baseRecipe, preRequisite), IShapedRecipe<CraftingInventory> {
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

    override fun getSerializer(): IRecipeSerializer<*> {
        return ModRecipeSerializers.RESEARCH_REQUIRED_SHAPED_RECIPE
    }
}