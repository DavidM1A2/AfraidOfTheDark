package com.davidm1a2.afraidofthedark.common.event.register;

import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register spell delivery method event and registers all of our spell delivery methods
 */
public class SpellDeliveryMethodRegister
{
    /**
     * Called by forge to register any of our spell delivery methods
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    public void registerSpellDeliveryMethods(RegistryEvent.Register<SpellDeliveryMethodEntry> event)
    {
        IForgeRegistry<SpellDeliveryMethodEntry> registry = event.getRegistry();
        // Register all spell delivery methods in our mod
        registry.registerAll(ModSpellDeliveryMethods.SPELL_DELIVERY_METHODS);
    }
}
