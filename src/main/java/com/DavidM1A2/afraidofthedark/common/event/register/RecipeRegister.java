package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register recipe event and registers all of our custom IRecipes
 */
public class RecipeRegister
{
    /**
     * Called by forge to register any of our recipes
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        IForgeRegistry<IRecipe> registry = event.getRegistry();

        // Register each item in our item list
        registry.registerAll(ModRecipes.MOD_RECIPES);
    }
}
