package com.davidm1a2.afraidofthedark.common.crafting

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.google.gson.JsonObject
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.ShapelessRecipe
import net.minecraft.network.PacketBuffer
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistryEntry

/**
 * Factory for research required shapeless recipes. This type of recipe just ensures the proper research is done first
 */
class ResearchRequiredShapelessRecipeSerializer : ForgeRegistryEntry<IRecipeSerializer<*>>(), IRecipeSerializer<ResearchRequiredShapelessRecipe> {
    init {
        registryName = ResourceLocation(Constants.MOD_ID, "research_required_shapeless_recipe")
    }

    override fun write(buffer: PacketBuffer, recipe: ResearchRequiredShapelessRecipe) {
        // Copy & Paste Start (from: SERIALIZER.write(buffer, recipe))
        buffer.writeString(recipe.group)
        buffer.writeVarInt(recipe.ingredients.size)

        for (ingredient in recipe.ingredients) {
            ingredient.write(buffer)
        }

        buffer.writeItemStack(recipe.recipeOutput)
        // Copy & Paste End (from: SERIALIZER.write(buffer, recipe))

        buffer.writeResourceLocation(recipe.preRequisite.registryName!!)
    }

    override fun read(recipeId: ResourceLocation, json: JsonObject): ResearchRequiredShapelessRecipe {
        // This recipe is based on the shapeless recipe, so start with parsing that
        val baseRecipe = SERIALIZER.read(recipeId, json)

        // Grab the pre-requisite recipe which is based on our research registry
        val preRequisite =
            ModRegistries.RESEARCH.getValue(ResourceLocation(JSONUtils.getString(json, "required_research")))!!

        // Return the research required shapeless recipe
        return ResearchRequiredShapelessRecipe(baseRecipe, preRequisite)
    }

    override fun read(recipeId: ResourceLocation, buffer: PacketBuffer): ResearchRequiredShapelessRecipe {
        val baseRecipe = SERIALIZER.read(recipeId, buffer)!!

        val preRequisite = ModRegistries.RESEARCH.getValue(buffer.readResourceLocation())!!

        return ResearchRequiredShapelessRecipe(baseRecipe, preRequisite)
    }
    /*


     */

    companion object {
        private val SERIALIZER = ShapelessRecipe.Serializer()
    }
}