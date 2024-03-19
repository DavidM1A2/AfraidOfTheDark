package com.davidm1a2.afraidofthedark.common.integration.jei

import com.davidm1a2.afraidofthedark.common.crafting.ResearchRequiredRecipeBase
import com.davidm1a2.afraidofthedark.common.crafting.ResearchRequiredShapedRecipe
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.util.Size2i

class CraftingCategoryExtension<T : ResearchRequiredRecipeBase<*>>(private val recipe: T) : ICraftingCategoryExtension {
    override fun setIngredients(ingredients: IIngredients) {
        ingredients.setInputIngredients(recipe.ingredients)
        ingredients.setOutput(VanillaTypes.ITEM, recipe.resultItem)
    }

    override fun getRegistryName(): ResourceLocation? {
        return recipe.id
    }

    override fun getSize(): Size2i? {
        return if (recipe is ResearchRequiredShapedRecipe) {
            Size2i(recipe.recipeWidth, recipe.recipeHeight)
        } else {
            null
        }
    }
}