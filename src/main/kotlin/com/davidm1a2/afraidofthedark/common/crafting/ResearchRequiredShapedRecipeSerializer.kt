package com.davidm1a2.afraidofthedark.common.crafting

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.google.gson.JsonObject
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.network.PacketBuffer
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistryEntry

/**
 * Factory for research required shaped recipes. This type of recipe just ensures the proper research is done first
 */
class ResearchRequiredShapedRecipeSerializer : ForgeRegistryEntry<IRecipeSerializer<*>>(), IRecipeSerializer<ResearchRequiredShapedRecipe> {
    init {
        registryName = ResourceLocation(Constants.MOD_ID, "research_required_shaped_recipe")
    }

    override fun toNetwork(buffer: PacketBuffer, recipe: ResearchRequiredShapedRecipe) {
        // Copy & Paste Start (from: SERIALIZER.write(buffer, recipe))
        buffer.writeVarInt(recipe.recipeWidth)
        buffer.writeVarInt(recipe.recipeHeight)
        buffer.writeUtf(recipe.group)

        for (ingredient in recipe.ingredients) {
            ingredient.toNetwork(buffer)
        }

        buffer.writeItem(recipe.resultItem)
        // Copy & Paste End (from: SERIALIZER.write(buffer, recipe))

        buffer.writeResourceLocation(recipe.preRequisite.registryName!!)
    }

    override fun fromJson(recipeId: ResourceLocation, json: JsonObject): ResearchRequiredShapedRecipe {
        // This recipe is based on the shaped ore recipe, so start with parsing that
        val baseRecipe = SERIALIZER.fromJson(recipeId, json)

        // Grab the pre-requisite recipe which is based on our research registry
        val preRequisite =
            ModRegistries.RESEARCH.getValue(ResourceLocation(JSONUtils.getAsString(json, "required_research")))!!

        // Return the research required shaped recipe
        return ResearchRequiredShapedRecipe(baseRecipe, preRequisite)
    }

    override fun fromNetwork(recipeId: ResourceLocation, buffer: PacketBuffer): ResearchRequiredShapedRecipe {
        val baseRecipe = SERIALIZER.fromNetwork(recipeId, buffer)!!

        val preRequisite = ModRegistries.RESEARCH.getValue(buffer.readResourceLocation())!!

        return ResearchRequiredShapedRecipe(baseRecipe, preRequisite)
    }

    companion object {
        private val SERIALIZER = ShapedRecipe.Serializer()
    }
}