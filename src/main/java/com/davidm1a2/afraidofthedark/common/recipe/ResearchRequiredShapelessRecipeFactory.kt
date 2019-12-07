package com.davidm1a2.afraidofthedark.common.recipe

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.google.gson.JsonObject
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.crafting.IRecipeFactory
import net.minecraftforge.common.crafting.JsonContext
import net.minecraftforge.oredict.ShapelessOreRecipe

/**
 * Factory for research required shapeless recipes. This type of recipe just ensures the proper research is done first
 */
class ResearchRequiredShapelessRecipeFactory : IRecipeFactory
{
    /**
     * Parses the recipe from JSON
     *
     * @param context The JSON context to parse with
     * @param json    The actual JSON to parse
     * @return The IRecipe representing this recipe
     */
    override fun parse(context: JsonContext, json: JsonObject): IRecipe
    {
        // This recipe is based on the shapeless ore recipe, so start with parsing that
        val baseRecipe = ShapelessOreRecipe.factory(context, json)

        // Grab the pre-requisite recipe which is based on our research registry
        val preRequisite = ModRegistries.RESEARCH.getValue(ResourceLocation(JsonUtils.getString(json, "required_research")))

        // Return the research required shaped recipe
        return ResearchRequiredShapelessRecipe(baseRecipe, preRequisite!!)
    }
}