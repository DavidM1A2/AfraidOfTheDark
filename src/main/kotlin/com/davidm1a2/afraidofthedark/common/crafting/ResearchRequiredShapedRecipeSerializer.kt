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
        setRegistryName(ResourceLocation(Constants.MOD_ID, "research_required_shaped_recipe"))
    }

    override fun write(buffer: PacketBuffer, recipe: ResearchRequiredShapedRecipe) {
        // Copy & Paste Start (from: SERIALIZER.write(buffer, recipe))
        buffer.writeVarInt(recipe.recipeWidth)
        buffer.writeVarInt(recipe.recipeHeight)
        buffer.writeString(recipe.group)

        for (ingredient in recipe.ingredients) {
            ingredient.write(buffer)
        }

        buffer.writeItemStack(recipe.recipeOutput)
        // Copy & Paste End (from: SERIALIZER.write(buffer, recipe))

        buffer.writeResourceLocation(recipe.preRequisite.registryName!!)
    }

    override fun read(recipeId: ResourceLocation, json: JsonObject): ResearchRequiredShapedRecipe {
        // This recipe is based on the shaped ore recipe, so start with parsing that
        val baseRecipe = SERIALIZER.read(recipeId, json)

        // Grab the pre-requisite recipe which is based on our research registry
        val preRequisite =
            ModRegistries.RESEARCH.getValue(ResourceLocation(JSONUtils.getString(json, "required_research")))!!

        // Return the research required shaped recipe
        return ResearchRequiredShapedRecipe(baseRecipe, preRequisite)
    }

    override fun read(recipeId: ResourceLocation, buffer: PacketBuffer): ResearchRequiredShapedRecipe {
        val baseRecipe = SERIALIZER.read(recipeId, buffer)!!

        val preRequisite = ModRegistries.RESEARCH.getValue(buffer.readResourceLocation())!!

        return ResearchRequiredShapedRecipe(baseRecipe, preRequisite)
    }

    companion object {
        private val SERIALIZER = ShapedRecipe.Serializer()
    }
}