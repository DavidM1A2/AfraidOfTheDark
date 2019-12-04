package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.recipe.ScrollCombineRecipe
import net.minecraft.item.crafting.IRecipe

/**
 * Class containing a list of custom mod recipes
 */
object ModRecipes
{
    // The recipe used to combine two or more recipes
    val SCROLL_COMBINE_RECIPE = ScrollCombineRecipe()

    // A list of mod recipes
    val MOD_RECIPES = arrayOf(
            SCROLL_COMBINE_RECIPE
    )
}