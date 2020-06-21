package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.recipe.ResearchRequiredShapedRecipeFactory
import com.davidm1a2.afraidofthedark.common.recipe.ResearchRequiredShapelessRecipeFactory
import com.davidm1a2.afraidofthedark.common.recipe.ScrollCombineRecipe
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.RecipeSerializers

object ModRecipeFactories {
    val RESEARCH_REQUIRED_SHAPED_RECIPE = ResearchRequiredShapedRecipeFactory()
    val RESEARCH_REQUIRED_SHAPELESS_RECIPE = ResearchRequiredShapelessRecipeFactory()
    val SCROLL_COMBINE_RECIPE = RecipeSerializers.SimpleSerializer<ScrollCombineRecipe>("${Constants.MOD_ID}:scroll_combine") { ScrollCombineRecipe() }

    val RECIPE_FACTORIES: List<IRecipeSerializer<out IRecipe>> = listOf(
        RESEARCH_REQUIRED_SHAPED_RECIPE,
        RESEARCH_REQUIRED_SHAPELESS_RECIPE,
        SCROLL_COMBINE_RECIPE
    )
}