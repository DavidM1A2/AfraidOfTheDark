package com.davidm1a2.afraidofthedark.common.recipe;

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries;
import com.davidm1a2.afraidofthedark.common.registry.research.Research;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Factory for research required shaped recipes. This type of recipe just ensures the proper research is done first
 */
public class ResearchRequiredShapedRecipeFactory implements IRecipeFactory
{
    /**
     * Parses the recipe from JSON
     *
     * @param context The JSON context to parse with
     * @param json    The actual JSON to parse
     * @return The IRecipe representing this recipe
     */
    @Override
    public IRecipe parse(JsonContext context, JsonObject json)
    {
        // This recipe is based on the shaped ore recipe, so start with parsing that
        ShapedOreRecipe baseRecipe = ShapedOreRecipe.factory(context, json);

        // Grab the pre-requisite recipe which is based on our research registry
        Research preRequisite = ModRegistries.RESEARCH.getValue(new ResourceLocation(JsonUtils.getString(json, "required_research")));

        // Return the research required shaped recipe
        return new ResearchRequiredShapedRecipe(baseRecipe, preRequisite);
    }
}
