package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class that receives the register spell delivery method event and registers all of our spell delivery methods
 */
class SpellDeliveryMethodRegister {
    /**
     * Called by forge to register any of our spell delivery methods
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerSpellDeliveryMethods(event: RegistryEvent.Register<SpellDeliveryMethod>) {
        val registry = event.registry

        // Register all spell delivery methods in our mod
        registry.registerAll(*ModSpellDeliveryMethods.SPELL_DELIVERY_METHODS)
    }
}