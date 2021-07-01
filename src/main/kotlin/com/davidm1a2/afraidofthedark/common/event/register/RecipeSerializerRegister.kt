package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModRecipeSerializers
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class RecipeSerializerRegister {
    /**
     * Called by forge to register any of our items
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<IRecipeSerializer<*>>) {
        val registry = event.registry

        registry.registerAll(*ModRecipeSerializers.LIST)
    }
}