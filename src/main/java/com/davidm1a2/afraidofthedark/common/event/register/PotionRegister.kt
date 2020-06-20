package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModPotions
import net.minecraft.potion.Potion
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that receives the register potion event and registers all of our potions
 */
class PotionRegister {
    /**
     * Called by forge to register any of our potions
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerPotions(event: RegistryEvent.Register<Potion>) {
        val registry = event.registry

        // Register each potion in our potion list
        registry.registerAll(*ModPotions.POTION_LIST)
    }
}