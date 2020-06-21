package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModRecipeFactories
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.RecipeSerializers

object RecipeFactoryRegister {
    fun register() {
        ModRecipeFactories.RECIPE_FACTORIES.forEach {
            @Suppress("UNCHECKED_CAST")
            RecipeSerializers.register(it as IRecipeSerializer<IRecipe>)
        }
    }
}