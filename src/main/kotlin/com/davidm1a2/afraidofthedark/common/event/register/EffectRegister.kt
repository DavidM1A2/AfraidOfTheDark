package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModEffects
import net.minecraft.potion.Effect
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that receives the register effect event and registers all of our effects
 */
class EffectRegister {
    /**
     * Called by forge to register any of our effects
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerPotions(event: RegistryEvent.Register<Effect>) {
        val registry = event.registry

        // Register each potion in our potion list
        registry.registerAll(*ModEffects.POTION_LIST)
    }
}