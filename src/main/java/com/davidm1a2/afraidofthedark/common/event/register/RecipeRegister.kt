package com.davidm1a2.afraidofthedark.common.event.register

/**
 * Class that receives the register recipe event and registers all of our custom IRecipes
 */
class RecipeRegister {
    /**
     * Called by forge to register any of our recipes
     *
     * @param event The event to register to
     */
    /*
    @SubscribeEvent
    fun registerRecipes(event: RegistryEvent.Register<IForgeRegistryEntry<IRecipe>>) {
        val registry = event.registry

        // Register each item in our item list
        registry.registerAll(*ModRecipes.MOD_RECIPES)

        RecipeSerializers.register(RecipeSerializers.SimpleSerializer("crafting_research_scroll") { ScrollCombineRecipe() })
    }
     */
}