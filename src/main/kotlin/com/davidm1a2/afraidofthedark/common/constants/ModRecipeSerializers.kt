package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.crafting.ResearchRequiredShapedRecipeSerializer
import com.davidm1a2.afraidofthedark.common.crafting.ResearchRequiredShapelessRecipeSerializer
import com.davidm1a2.afraidofthedark.common.crafting.ScrollCombineRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleRecipeSerializer

object ModRecipeSerializers {
    val RESEARCH_REQUIRED_SHAPED_RECIPE = ResearchRequiredShapedRecipeSerializer()
    val RESEARCH_REQUIRED_SHAPELESS_RECIPE = ResearchRequiredShapelessRecipeSerializer()
    val SCROLL_COMBINE_RECIPE: RecipeSerializer<*> = SimpleRecipeSerializer { _ -> ScrollCombineRecipe() }.setRegistryName(Constants.MOD_ID, "scroll_combine")

    val LIST = arrayOf(
        RESEARCH_REQUIRED_SHAPED_RECIPE,
        RESEARCH_REQUIRED_SHAPELESS_RECIPE,
        SCROLL_COMBINE_RECIPE
    )
}