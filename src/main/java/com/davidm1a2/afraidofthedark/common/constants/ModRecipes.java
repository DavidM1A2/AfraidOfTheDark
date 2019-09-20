package com.davidm1a2.afraidofthedark.common.constants;

import com.davidm1a2.afraidofthedark.common.recipe.ScrollCombineRecipe;
import net.minecraft.item.crafting.IRecipe;

/**
 * Class containing a list of custom mod recipes
 */
public class ModRecipes
{
    // The recipe used to combine two or more recipes
    public static final IRecipe SCROLL_COMBINE_RECIPE = new ScrollCombineRecipe();

    // A list of mod recipes
    public static final IRecipe[] MOD_RECIPES = new IRecipe[]
            {
                    SCROLL_COMBINE_RECIPE
            };
}
