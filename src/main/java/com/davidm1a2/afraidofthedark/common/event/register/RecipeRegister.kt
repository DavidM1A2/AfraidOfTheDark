package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModRecipes
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Class that receives the register recipe event and registers all of our custom IRecipes
 */
class RecipeRegister {
    /**
     * Called by forge to register any of our recipes
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerRecipes(event: RegistryEvent.Register<IRecipe>) {
        val registry = event.registry

        // Register each item in our item list
        registry.registerAll(*ModRecipes.MOD_RECIPES)
    }
}